package org.cloudio.morpheus.chat

import org.morpheus.{dimension, fragment}

/**
 * Created by zslajchrt on 21/03/15.
 */
class Utils {

}

case class EmailMessage(to: String, from: String, subject: String, body: String)

@dimension
trait EmailService {

  def sendEmailMessage(message: EmailMessage)

}

@fragment
trait MockEmailService extends EmailService {

  def sendEmailMessage(message: EmailMessage): Unit = {
    println(message)
  }
}