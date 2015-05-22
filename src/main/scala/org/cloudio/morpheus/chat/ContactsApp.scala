package org.cloudio.morpheus.chat

import org.morpheus.Morpheus._

/**
*
* Created by zslajchrt on 18/03/15.
*/
object ContactsApp {

  def main(args: Array[String]) {

    implicit val contactListConfig = frag[ContactList, ContactListConfig](ContactListConfig(2))

    //val contactListComp = compose[ContactList with ContactListPersistence with MockContactsDAO]

    val contactListComp = compose[ContactList with PrettyPrintedContactList with ValidatingContactList with ContactListPersistence with MockContactsDAO with MockContactTypes]
    val contactList: ContactTypes with ContactList with Presentable with Validating with ContactListPersistence with MockContactsDAO with MockContactTypes = contactListComp.make
//    val contactList = new ContactList with PrettyPrintedContactList with ValidatingContactList with ContactListPersistence with MockContactsDAO with MockContactTypesStatic {
//      override val ownerId: Option[Long] = None
//    }

    // todo: Both contactList and contactList2 should have the same functionality

    contactList.initContactPersistence()
    contactList.loadContacts()

    println(s"Owner: ${contactList.owner.present}") // todo: nice
    println(s"Contacts ${contactList.present}")

    def addContact() = {
      val newContact = contactList.loadContact(None)
      newContact.name = "pepa"
      newContact.email = "pepa@seznam.cz"
      contactList.addToContactList(newContact)
      newContact
    }

    val newContact1 = addContact()
    newContact1.name += "1"
    newContact1.persist()
    val newContact2 = addContact()
    val newContact3 = addContact()

    println(s"More contacts ${contactList.present}")

    contactList.removeFromContactList(newContact1)
    contactList.removeFromContactList(newContact2)
    contactList.removeFromContactList(newContact3)

    println(s"Less contacts ${contactList.present}")
    println(s"Contacts are valid: ${contactList.isValid}")
  }

}
