package org.cloudio.morpheus.chat

import java.util.concurrent.atomic.AtomicLong

import org.morpheus.{dimension, wrapper, fragment}
import org.morpheus.Morpheus._
import org.morpheus._

import scala.util.Random

/**
*
* Created by zslajchrt on 19/03/15.
*/

@fragment
trait LiveContactList {

  this: MutableFragment =>

  def online_=(ol: Boolean): Unit = {
    fire("Online", ol, this)
  }
}

@dimension
trait ContactListState {
  def online: Boolean
}

@fragment
trait OnlineContactList extends ContactListState {
  def online: Boolean = true
}

@fragment
trait OfflineContactList extends ContactListState {
  def online: Boolean = false
}

sealed trait ContactStatus
case object OFFLINE extends ContactStatus
case object BUSY extends ContactStatus
case object AVAILABLE extends ContactStatus
case object GONE extends ContactStatus

@fragment
trait LiveContact {
  this: Contact with ContactState with MutableFragment =>

  //private [this] var _status: ContactStatus = GONE

  def status_=(s: ContactStatus): Unit = {
    //_status = s
    fire("ContactStatus", s, this)
  }

  def isOffline: Boolean = status == OFFLINE
  def isBusy: Boolean = status == BUSY
  def isAvailable: Boolean = status == AVAILABLE
  def isGone: Boolean = status == GONE
}

@dimension
trait ContactState {
  def status: ContactStatus
}

@fragment
trait OfflineContact extends ContactState {

  this: Contact with LiveContact with EmailService =>

  def sendEmail(subject: String, body: String) {
    sendEmailMessage(EmailMessage(email, "", subject, body))
  }

  override def status: ContactStatus = OFFLINE
}

trait TextOutput {
  def write(text: String)
}

trait OnlineContact extends TextOutput with ContactState  {

  override def write(text: String): Unit = {
    // todo
  }

}

@fragment
trait AvailableContact extends OnlineContact {

  this: LiveContact =>

  override def status: ContactStatus = AVAILABLE

}

@fragment
trait BusyContact extends OnlineContact {

  this: LiveContact =>

  override def status: ContactStatus = BUSY

}

@fragment
trait GoneContact extends ContactState {

  this: Contact =>

  override def status: ContactStatus = GONE

}

@fragment @wrapper
trait PrettyPrintedLiveContact extends PrettyPrintedContact {
  this: Contact with ContactState with LiveContact =>

  override def present: String = {
    val s = this match {
      case bc: BusyContact => "Is busy"
      case bc: AvailableContact => "Is available"
      case bc: GoneContact => "Is gone"
      case bc: OfflineContact => "Is offline"
      case _ => "?"
    }

    s"Status: $status, ${super.present}, $s"
  }
}

@fragment @wrapper
trait PrettyPrintedLiveContactList extends PrettyPrintedContactList {
  this: ContactList with HasPrettyPrintedContact with LiveContactList =>

  override def present: String = {
    val status = this match {
      case bc: OfflineContactList => "OFFLINE"
      case bc: OnlineContactList => "ONLINE"
      case _ => "?"
    }

    s"$status ${super.present}"
  }
}

@fragment
trait ContactRandomizer {
  this: Contact with LiveContact with  MutableFragment =>

  def start(): Unit = {
    // the context 'this' referring to the composite proxy is copied to the new thread (InheritableThreadLocal)
    new Thread() {

      override def run(): Unit = {

        while (true) {
          //val s = mirror(ContactRandomizer.this)

          Thread.sleep(2000)


          // Q: Why accessing the 'status' member work? In other words, how come 'ContactRandomizer.this' in this thread correctly refers the composite proxy?
          // A: Because this thread was created in the start method when the composite proxy was associated
          // with the calling thread via an inheritable thread local variable. The value of this variable, i.e. the composite proxy,
          // was cloned for this thread.

          status_=(Random.nextInt(3) match {
            case 0 => BUSY
            case 1 => AVAILABLE
            case 2 => GONE
            case _ => sys.error("Should not be here")
          })
        }
      }
    }.start()
  }
}

@fragment
trait MockLiveContactTypes extends ContactTypes with HasPrettyPrintedContact {
  this: ContactListState with ContactsDAO =>

  type CompositeContactType = Contact with
    ContactPersistence with PrettyPrintedContact with PrettyPrintedLiveContact with LiveContact with
    ((OfflineContact with MockEmailService) or AvailableContact or BusyContact or GoneContact) with MutableFragment with ContactRandomizer
  val contactModel = parse[CompositeContactType](true)
  type ContactType = contactModel.MutableLUB

  //type ContactType = contactModel.MutableLUB

  private val idGen = new AtomicLong()

  def specToXXX(contact: ContactType): &[OfflineContact] = {
    contact.toCompositeInstance
  }

  def specToXXY(contact: ContactType): &[/?[OfflineContact with $[({type w = EmailService@dimension @wrapper})#w]]] = {
    contact.toCompositeInstance
  }

  implicit val conv1 = specToXXX _
  implicit val conv2 = specToXXY _

  def createContact(id: Option[Long]): ContactType = {
    val morphEvent = CompositeEvent("morphEvent", null, null)
    val contactStatusMonitor = EventMonitor[ContactStatus]("ContactStatus", morphEvent)


    def isOnline: Boolean = {
      //val invokingMutableInstance = this.asInstanceOf[CompositeMirror[_, _]].owningMutableProxy.get.asInstanceOf[ContactListState]
      //select[ContactListState](this).get.online
      //invokingMutableInstance.online
      (for (m <- mirror(this);
           mm <- m.owningMutableProxy;
           state <- select[ContactListState](mm)) yield state.online)
        .getOrElse(false)
    }

    new {
      def a(): Unit = {
        select[ContactListState](MockLiveContactTypes.this).get.online
      }
    }

    import contactModel._

    val strategy = activator(
      ?[OfflineContact] { _ => !isOnline} orElse
        ?[GoneContact] { _ => isOnline && contactStatusMonitor(GONE, true)} orElse
        ?[AvailableContact] { _ => isOnline && contactStatusMonitor(AVAILABLE, false)} orElse
        ?[BusyContact] { _ => isOnline && contactStatusMonitor(BUSY, false)}
    )

    implicit val mutableFragConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig(contactStatusMonitor))
    implicit val contactConfig = single[Contact, ContactConfig](ContactConfig(id.getOrElse(idGen.getAndIncrement)))
    implicit val contactPersConfig = single[ContactPersistence, ContactPersistenceConfig](ContactPersistenceConfig(this))

    val contactComp = singleton(contactModel, strategy)
    val mp = contactComp.~
    mp.startListening(morphEvent.nameSelector)

    mp.start

    mp
  }

}

//@fragment
//trait MockLiveContactsDAO extends MockContactsDAO {
//  this: ContactTypes =>
//}

@fragment
trait ContactsMessenger {

  this: ContactList with ContactTypes =>

  // This method on ContactList tries to reach all contacts by their email. Only OfflineContact has a method for sending email.
  def sendMessageToContact(subject: String, body: String)(implicit c: (ContactType) => &[OfflineContact]) {

    for (contact <- contacts) {
      val offlineContact = *(contact).make
      offlineContact.sendEmail(subject, body)
    }

  }

  def sendMailToOfflineContactsWithCustomFromField(from: String, subject: String, body: String)(implicit c: (ContactType) => &[/?[OfflineContact with $[({type w = EmailService@dimension @wrapper})#w]]]) {

    def toOfflineContact(contact: ContactType): Option[OfflineContact] = {
      val myEmailService = single[CustomFromFieldEmailService, CustomFromFieldEmailServiceConfig](CustomFromFieldEmailServiceConfig(from))
      val contactRef: &[/?[OfflineContact with $[({type w = EmailService@dimension @wrapper})#w]]] = contact
      val blended = *(contactRef, myEmailService)
      val m = blended.make_~
      println(m.myAlternative)
      Some(asCompositeOf[OfflineContact](blended))
      val s = select[OfflineContact](m)
      s
    }

    for (contact <- contacts;
         offlineContact <- toOfflineContact(contact)) {
      offlineContact.sendEmail(subject, body)
    }
  }

}


//@config // it causes that the companion object is generated
trait CustomFromFieldEmailServiceConfig {
  val customFrom: String
}

object CustomFromFieldEmailServiceConfig {
  def apply(from: String) = new CustomFromFieldEmailServiceConfig {
    override val customFrom: String = from
  }
}

@wrapper @dimension
trait CustomFromFieldEmailService extends EmailService with CustomFromFieldEmailServiceConfig {
//trait CustomFromFieldEmailService extends EmailService {
  abstract override def sendEmailMessage(message: EmailMessage): Unit = {
    super.sendEmailMessage(message.copy(from = customFrom))
  }

}


