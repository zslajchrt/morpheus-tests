package org.cloudio.morpheus.chat

import org.morpheus._
import org.morpheus.Morpheus._

/**
*
* Created by zslajchrt on 19/03/15.
*/
object LiveContactsApp {

  def main(args: Array[String]) {
    val morphEvent = CompositeEvent("morphEvent", null, null)
    val appStatusMonitor = EventMonitor[Boolean]("Online", morphEvent)

    val contactListModel = parse[ContactList with
          ContactListPersistence with
          PrettyPrintedContactList with PrettyPrintedLiveContactList with
          MockContactsDAO with MockLiveContactTypes with
          LiveContactList with (OnlineContactList or OfflineContactList) with
          ContactsMessenger with MutableFragment
        ](true)

    import contactListModel._

    val strategy = activator(
      ?[OnlineContactList] { _ => appStatusMonitor(true, false)} orElse
        ?[OfflineContactList] { _ => appStatusMonitor(false, true)}
    )

    implicit val mutableFragConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig(appStatusMonitor))
    implicit val contactListConfig = Morpheus.single[ContactList, ContactListConfig](ContactListConfig(0))

    val contactListComp = singleton(contactListModel, strategy)
    import contactListComp._

    //val contactList = contactListComp.morph_~(strategy)
    val contactList = contactListComp.~
    val isListening = contactList.startListening(morphEvent.nameSelector)

    contactList.initContactPersistence()
    contactList.loadContacts()

    select[PrettyPrintedContactList with OfflineContactList](contactList) match {
      case None =>
        sys.error("")
      case Some(offlineList) =>
        println(offlineList.present)
    }

    println(contactList.myAlternative)
    println("Is online: " + contactList.online)
    contactList.online = true
    println(contactList.myAlternative)
    println("Is online: " + contactList.online)
    //contactList.remorph()

    import contactList._ // there are an implicit conversion needed by the following statement
    contactList.sendMessageToContact("subject", "body")
    println("***")
    //contactList.sendMailToOfflineContactsWithCustomFromField("myfrom", "subject", "body")

    while (true) {
      Thread.sleep(2000)
      select[PrettyPrintedContactList with OnlineContactList](contactList) match {
        case None =>
          sys.error("")
        case Some(onlineList) =>
          println(onlineList.present)
      }
    }
  }

}
