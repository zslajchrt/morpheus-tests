//package org.cloudio.morpheus.samples
//
//
///**
// * Created by zslajchrt on 13/01/15.
// *
// */
//class Users {
//
//}
//
///**
// * Created by zslajchrt on 11/01/15.
// *
// */
//// entity
//class User(userName: String) {
//  private[this] var _name: String = userName
//
//  def name: String = _name
//
//  def name_=(value: String): Unit = _name = value
//
//  def this(image: User) = this(image.name)
//}
//
//// persistence
//
//trait UserPersistence {
//
//  def store: UserPersistence
//
//  def refresh: UserPersistence
//
//}
//trait UserPersistenceImp extends UserPersistence {
//  this: User with UserRepository =>
//
//  def store = {
//    store(this)
//    this
//  }
//
//  def refresh = {
//    refresh(this)
//    this
//  }
//}
//
//trait UserRepository {
//  protected def store(user: User): Unit
//
//  protected def refresh(user: User): Unit
//}
//
//// UserPersistence dim
//object UserPersistenceImpFragment extends FragmentType[UserPersistenceImp](UserDim, UserRepositoryDim)
//
//object UserPersistenceDim extends Dim[UserPersistence](UserPersistenceImpFragment)
//
//// a higher level trait
//trait UserRepositoryJPA extends UserRepository {
//
//  val em: EntityManager
//
//  protected def store(user: User) {
//    em.merge(user)
//  }
//
//  protected def refresh(user: User) {
//    em.refresh(user)
//  }
//}
//
//abstract class UserRepositoryJPAfragment extends UserRepositoryJPA {
//  private[UserRepositoryJPAfragment] val em: EntityManager = null
//}
//
//trait UserRepositoryMongo extends UserRepository {
//
//  val mm: MongoManager
//
//  protected def store(user: User) {
//    // ...
//  }
//
//  protected def refresh(user: User) {
//    // ...
//  }
//}
//
//// UserRepository dim
//object UserRepositoryJPAFragment extends FragmentType[UserRepositoryJPA]
//
//object UserRepositoryMongoFragment extends FragmentType[UserRepositoryMongo]
//
//object UserRepositoryDim extends Dim[UserRepository](UserRepositoryJPAFragment, UserRepositoryMongoFragment)
//
//// Other persistence stuff
//
//trait EntityManager {
//  def merge(e: AnyRef): Unit
//
//  def refresh(e: AnyRef): Unit
//}
//
//trait MongoManager {
//
//}
//
//
//
//// presentation
//
//case class PresentedUser(userName: String)
//
//trait UserPresentation[F] {
//  def toForm: F
//
//  def fromForm(form: F)
//}
//
//trait UserPresentationImp[F] extends UserPresentation[F] {
//  this: User with UserFormGate[F] =>
//
//  def toForm: F = {
//    externalize(PresentedUser(name))
//  }
//
//  def fromForm(form: F) = {
//    val presentedUser = internalize(form)
//    // update the user
//    name = presentedUser.userName
//  }
//
//}
//
//// UserPresentation dim
//object UserPresentationImpFragment extends FragmentType[UserPresentationImp[Json]](UserDim, UserFormGateJSONDim)
//
//object UserPresentationJSONDim extends Dim[UserPresentation[Json]](UserPresentationImpFragment)
//
//
//trait UserFormGate[F] {
//  protected def externalize(user: PresentedUser): F
//
//  protected def internalize(form: F): PresentedUser
//}
//
//trait UserFormGateJSON extends UserFormGate[Json] {
//  val prettyPrint: Boolean
//
//  protected def externalize(user: PresentedUser): Json = Json.toJson(user, prettyPrint)
//
//  protected def internalize(form: Json): PresentedUser = PresentedUser(form.get("userName"))
//}
//
//trait UserFormGateJSONBasic extends UserFormGateJSON {
//  val prettyPrint = false
//}
//
//
//trait UserFormGateJSONPrettyPrint extends UserFormGateJSON {
//  val prettyPrint = true
//}
//
//// UserFormGate dim
//object UserFormGateJSONPrettyPrintFragment extends FragmentType[UserFormGateJSONPrettyPrint]
//
//object UserFormGateJSONBasicFragment extends FragmentType[UserFormGateJSONBasic]
//
//object UserFormGateJSONDim extends Dim[UserFormGate[Json]](UserFormGateJSONBasicFragment, UserFormGateJSONPrettyPrintFragment)
//
//// Json API
//
//class Json {
//  def set(field: String, value: String) {}
//
//  def get(field: String): String = null // todo
//}
//
//object Json {
//  def toJson(o: Any, prettyPrint: Boolean): Json = {
//    null
//  }
//}
//
//trait UserValidator extends User {
//  override def name_=(value: String): Unit = {
//    require(value != null, "User name must not be null")
//    super.name_=(value)
//  }
//}
//
//case class Natural(value: StringBuilder) extends Proxy {
//  def self = value
//}
//
//object UserApp {
//
//  def main(args: Array[String]): Unit = {
//
//    trait N {
//      def a(i: Int): Int = 2 * i
//    }
//
//    trait M extends N {
//      override def a(i: Int): Int = super.a(3 * i)
//    }
//
//    trait O extends N {
//      override def a(i: Int): Int = super.a(i + 1)
//    }
//
//    class Z extends O with M with N {
//      //override def a(i: Int): Int = super.a(5 * i)
//    }
//
//    println(new Z a 1)
//
//    val userName = args(0)
//
//    // Entity pipeline: SQL db -> JSON presentation and modification -> Mongo
//
//    // Composition1: load the user
//    val entityManager: EntityManager = null // todo
//    val persistentUser = new User(userName)
//        with UserValidator
//        with UserPersistenceImp
//        with UserRepositoryJPA {
//        val em: EntityManager = entityManager
//      }
//
//    persistentUser.refresh
//
//    //    // Composition 2: display user
//    //    val presentableUser = new User(persistentUser)
//    //      with UserValidator
//    //      with UserPresentation[Json]
//    //      with UserFormGateJSONPrettyPrint
//    //
//    //    val form: Json = presentableUser.toForm
//    //    form.set("userName", "Josef")
//    //    presentableUser.fromForm(form)
//    //
//    //    // Composition 3: store the user to another storage
//    //    val mongoManager: MongoManager = null // todo
//    //    val persistenceUser2 = new User(presentableUser)
//    //        with UserPersistenceImp
//    //        with UserRepositoryMongo {
//    //        override val mm = mongoManager
//    //      }
//    //
//    //    persistenceUser2.store()
//  }
//
//}
//
//
