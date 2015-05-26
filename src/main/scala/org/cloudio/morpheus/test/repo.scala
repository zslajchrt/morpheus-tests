package org.cloudio.morpheus.test

import org.morpheus.{dimension, fragment, wrapper}
import org.morpheus.Morpheus._
import org.morpheus.Morpher._
import org.morpheus._

//import org.morpheus.Composite

/**
 * Created by zslajchrt on 01/03/15.
 */

class repo

trait EntityBase {
  val id: Int
}

case class EntityA(id: Int) extends EntityBase {
  def this() = this(0)

  /**
   * This method is overridden by both EntityAValidator and EntityALogger
   */
  def methodX(x: Int) = {
    id + x
  }

  /**
   * This method is overridden by EntityAValidator only
   */
  def methodY(y: Int) = {
    id - 2 * y
  }

  /**
   * This method is overridden by EntityALogger only
   */
  def methodZ(z: Int) = {
    id - 3 * z
  }

  def methodR(r: Int) = {
    2 * helperR(r)
  }

  protected def helperR(r: Int): Int = {
    r
  }
}

@fragment
@wrapper
trait EntityAValidator extends EntityA {

  var counterValidatorX = 0
  var counterValidatorY = 0

  override def methodX(x: Int): Int = {
    counterValidatorX += 1
    super.methodX(x)
  }

  override def methodY(y: Int): Int = {
    counterValidatorY += 1
    super.methodY(y)
  }

  def methodY(b: Boolean) = !b
}

@fragment
@wrapper
trait EntityALogger extends EntityA {

  var counterLoggerX = 0
  var counterLoggerZ = 0

  override def methodX(x: Int): Int = {
    counterLoggerX += 1
    super.methodX(x)
  }

  override def methodZ(z: Int): Int = {
    counterLoggerZ += 1
    super.methodZ(z)
  }

  def logLevel = 1
}

@fragment
@wrapper
// todo: This does not work - entity wrappers cannot be invoked from within the entity
trait EntityAHelper extends EntityA {
  override protected def helperR(r: Int): Int = {
    -super.helperR(r)
  }
}

@fragment
trait Ping {
  this: Pong =>

  var pingCounter = 0

  var methodXCounterInPing = 0
  var methodYCounterInPing = 0
  var methodZCounterInPing = 0

  def ping(n: Int): Int = {
    pingCounter += 1
    pong(n)
  }

  def methodX(n: Int): Int = {
    methodXCounterInPing += 1
    1
  }

  def methodY(n: Int): Int = {
    methodYCounterInPing += 1
    1
  }

  def methodZ(n: Int): Int = {
    methodZCounterInPing += 1
    1
  }

  //  def newPing() = new Initializer[Ping] {
  //
  //    override def initialize(fragment: Ping): Ping = {
  //      fragment.pingCounter = pingCounter;
  //      fragment
  //    }
  //  }
  //
  //  def newPing2() = new {
  //    type Model = Ping with \?[PingValidator] with \?[PingLogger]
  //    implicit val strat: MorpherStrategy[Model] = new LeftAltsMorpherStrategy[Model]
  //  }
  //
  //  def newPingTrainer() = new {
  //    type Model = PingTrainer
  //    implicit val strat = new LeftAltsMorpherStrategy[Model]
  //  }

}

@fragment
@wrapper
trait PingValidator extends Ping {
  this: Pong =>

  var pingValidatorCounter = 0
  var methodXCounterInValidator = 0
  var methodYCounterInValidator = 0

  override def ping(n: Int): Int = {
    // validate the argument, e.g.
    pingValidatorCounter += 1
    super.ping(n)
  }

  override def methodX(n: Int): Int = {
    methodXCounterInValidator += 1
    super.methodX(n)
  }

  override def methodY(n: Int): Int = {
    methodYCounterInValidator += 1
    super.methodY(n)
  }
}

@fragment
@wrapper
trait PingLogger extends Ping {
  this: Pong =>

  var pingLoggerCounter = 0
  var methodXCounterInLogger = 0
  var methodZCounterInLogger = 0

  override def ping(n: Int): Int = {
    pingLoggerCounter += 1
    super.ping(n)
  }

  override def methodX(n: Int): Int = {
    methodXCounterInLogger += 1
    super.methodX(n)
  }

  override def methodZ(n: Int): Int = {
    methodZCounterInLogger += 1
    super.methodZ(n)
  }

}

trait PongConfig {
  val maxReturns: Int
}

object PongConfig extends PongConfig {
  override val maxReturns: Int = 10

  def cfg = Morpheus.single[Pong, PongConfig](PongConfig)
}

@fragment
trait Pong extends PongConfig {
  this: Ping =>

  var pongCounter = 0

  def pong(n: Int): Int = {
    pongCounter += 1
    if (n < maxReturns)
      ping(n + 1)
    else
      n
  }

  //  def newPong() = new Configurator[Pong, PongConfig] {
  //    val config = new PongConfig {
  //      val maxReturns: Int = Pong.this.maxReturns + 1
  //    }
  //
  //    override def initialize(fragment: Pong): Pong = {
  //      fragment.pongCounter = 2 * pongCounter
  //      fragment
  //    }
  //  }
  //
  //  def newPong2() = new {
  //    type Model = Pong with \?[PongCloneDecorator]
  //
  //    implicit val pongConfig = frag[Pong, PongConfig](new PongConfig {
  //      val maxReturns: Int = 5
  //    })
  //
  //    implicit val morphStrategy = activator[Model](
  //      ?[PongCloneDecorator] { _ => pongCounter > 5} // the strategy depends on the Pong's state
  //    )
  //  }
  //
  //  def newPongTrainer() = new {
  //    type Model = PongTrainer
  //    implicit val strat = new LeftAltsMorpherStrategy[Model]
  //  }

}

//@fragment
//@wrapper
//trait PongCloneDecorator extends Pong {
//  this: Ping =>
//
//  override def newPong() = {
//    val cloned = super.newPong()
//    new Configurator[Pong, PongConfig] {
//      val config: PongConfig = cloned.config
//
//      override def initialize(fragment: Pong): Pong = {
//        val f = super.initialize(fragment)
//        f.pongCounter += 100
//        f
//      }
//    }
//  }
//}

//trait PingTrainerConfigBase {
//  val x: String
//}

@fragment
trait PingTrainer {
  this: PongTrainer with Ping =>

  def pingReady(): Boolean = {
    pingCounter == 0
  }
}

@fragment
trait PongTrainer {
  this: PingTrainer with Pong =>

  def pongReady(): Boolean = {
    pongCounter == 0
  }
}

trait Printable {

  def print(): String

}

@fragment
trait EntityAJSONPrinter extends Printable {

  this: EntityBase =>

  def print(): String = {
    s"{'id': $id}"
  }
}

@fragment
trait EntityACSVPrinter extends Printable {
  // A fragment cannot depend on an entity class. Only traits are allowed.
  this: EntityBase =>

  def print(): String = {
    s"$id"
  }
}

// volatile polymorphism

trait Food {

}

@fragment
trait Apple extends Food {
  override def toString: String = "Apple"
}

@fragment
trait Fish extends Food {
  override def toString: String = "Fish"
}

@fragment
trait Animal {

  // todo: Emphasize the fact that MutableFragment is an non-generated fragment imported from a dependency.
  // todo: For a non-generated fragment there must be its fragment-class counter-part whose name ends with '$fragment'
  this: MutableFragment =>

  private[this] var _carnivore: Boolean = false
  private[this] var _alive: Boolean = true

  def carnivore: Boolean = _carnivore

  def carnivore_=(flag: Boolean) {
    _carnivore = flag
    fire("foodPref", flag, this)
  }

  def kill() {
    _alive = false
  }

  def isAlive = _alive

  // the animal model is 'singleton', i.e. 'mutableProxy' invocation does not create new fragments
  val craveForModel = singleton[Apple or Fish]

  lazy val craveFor: craveForModel.MutableLUB = {

    val morphEvent= CompositeEvent("morphEvent", null, null)
    val carnivoreMonitor = new EventMonitor[Boolean]("foodPref", morphEvent)
    addListener(carnivoreMonitor)

    implicit val strategy = craveForModel.activator(
      ?[Apple] { _ => carnivoreMonitor(false, true)} orElse
        ?[Fish] { _ => carnivoreMonitor(true, false)})

    val proxy = craveForModel.morph_~
    proxy.startListening(morphEvent.nameSelector)
    addListener(new MutableFragmentListener {
      override def onEvent(eventName: String, eventValue: Any, eventSource: Any): List[CompositeEvent[_]] = {
        if (morphEvent.eventName == eventName) {
          proxy.remorph()
         }
        Nil
      }
    })
    proxy
  }


  override def toString: String = {
    s"Having a crave for $craveFor"
  }
}

@fragment
trait MovingAnimal {
  this: Animal =>

  var pos = (0, 0)

  def move(dx: Int, dy: Int): Unit = {
    pos = (pos._1 + dx, pos._2 + dy)
  }

}

//todo: make @wrapper annotation optional since its meaning can be derived from the parents list
@wrapper
@fragment
trait LiveObserver extends Animal {

  this: MutableFragment =>

  override def kill(): Unit = {
    super.kill()
    fire("killed", true, this)
  }
}

@fragment
trait Herd {

  // the animal model is composed, i.e. each 'add' invocation creates new fragments
  val animalModel = parse[Animal with LiveObserver with MutableFragment with \?[MovingAnimal]](true)

  private var _members: List[animalModel.MutableLUB] = Nil

  def add(): animalModel.MutableLUB = {
    val morphEvent = CompositeEvent("morphEvent", null, null)
    val killMonitor = EventMonitor[Boolean]("killed", morphEvent)

    val strategy = animalModel.activator(
      ?[MovingAnimal] { frag => killMonitor(false, true)}
    )

    implicit val mutableFragConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig(killMonitor))

    val newAnimal = compose(animalModel, strategy).~
    newAnimal.startListening(morphEvent.nameSelector)

    _members ::= newAnimal
    _members.head
  }

  def members() = {
    _members
  }

}

@fragment
trait Photo {
  //todo @property macro annotation expanding the var declaration into the getter/setter pair
  var id: Long = _
  var name: String = _

  private[this] var _width: Int = _

  def width = _width

  def width_=(w: Int): Unit = {
    _width = w
  }

  private[this] var _height: Int = _

  def height = _height

  def height_=(h: Int): Unit = {
    _height = h
  }

  override def toString: String = s"Photo(id=$id, name=$name, width=$width, height=$height)"
}

trait BigPhotoConfig {
  val iconSize: (Int, Int)
}

object BigPhotoConfig {
  def apply(iconWidth: Int, iconHeight: Int) = new BigPhotoConfig {
    val iconSize: (Int, Int) = (iconWidth, iconHeight)
  }
}

@fragment
trait BigPhoto extends BigPhotoConfig {
  this: Photo with PhotoEditor =>

  def iconize(): Unit = {
    name += "Icon"
    resizeAbsolute(iconSize._1, iconSize._2)
  }

  override def toString: String = "BigPhoto:" + super.toString
}

@fragment
trait IconPhoto {
  this: Photo =>

  override def toString: String = "Icon:" + super.toString

}

trait Album {

  type PhotoType <: Photo

  def newPhoto(id: Long): PhotoType

  private var _photos: List[PhotoType] = Nil

  def addPhoto(photo: PhotoType) = {
    _photos ::= photo
  }

  def findPhoto(photoName: String): Option[PhotoType] = {
    _photos.find(_.name == photoName)
  }

  def photos: List[PhotoType] = _photos

}

@fragment
trait AlbumLoader {
  this: Album =>

  def loadPhotos(ids: List[Long]): Unit = {
    for (id <- ids) {
      val photo: PhotoType = loadPhoto(id)
      addPhoto(photo)
    }
  }

  protected def loadPhoto(id: Long): PhotoType = {
    val photo: PhotoType = newPhoto(id)
    photo.id = id
    photo.name = s"Photo$id"
    photo.width = id.toInt * 100
    photo.height = id.toInt * 200
    photo
  }
}

@fragment
trait PhotoEditor {
  this: Photo =>

  def rotate(angle: Double): Unit = {
    // todo
  }

  def resize(wFact: Double, hFact: Double): Unit = {
    width = math.round(width * wFact).toInt
    height = math.round(height * wFact).toInt
  }

  def resizeAbsolute(w: Int, h: Int): Unit = {
    width = w
    height = h
  }
}

trait EditObserverConfig extends BigPhotoConfig

@fragment
@wrapper
trait EditObserver extends Photo with EditObserverConfig {

  this: MutableFragment =>

  def isIcon: Boolean = {
    width <= iconSize._1 && height <= iconSize._1
  }

  private def detectChange(act: => Unit): Unit = {
    val wasIcon = isIcon
    act
    val isNowIcon = isIcon
    if ((wasIcon && !isNowIcon) || (!wasIcon && isNowIcon))
    //      println(s"PhotoTypeChange:$this")
      fire("PhotoTypeChange", (), this)
  }

  override def width_=(w: Int): Unit = {
    detectChange {
      super.width_=(w)
    }
  }

  //override def width: Int = super.width

  override def height_=(h: Int): Unit = {
    detectChange {
      super.height_=(h)
    }
  }

  //override def height: Int = super.height
}

trait AlbumEditorConfig extends BigPhotoConfig with EditObserverConfig {
}

object AlbumEditorConfig {
  def apply(iconWidth: Int, iconHeight: Int) = new AlbumEditorConfig {
    val iconSize: (Int, Int) = (iconWidth, iconHeight)
  }
}

@fragment
trait AlbumEditor extends Album with AlbumEditorConfig {
  // todo: detect a wrong wrapper position, like here, where EditObserver must wrap the Photo fragment since it is a Photo wrapper
  //val photoModel = parse[Photo with EditObserver with PhotoEditor with MutableFragment with (BigPhoto or IconPhoto)]

  val photoModel = parse[Photo with EditObserver with PhotoEditor with MutableFragment with (BigPhoto or IconPhoto)](true)

  type PhotoType = photoModel.MutableLUB

  def newPhoto(id: Long): PhotoType = {
    implicit val bigPhotoConfig = single[BigPhoto, BigPhotoConfig](this)
    implicit val editObserverConfig = single[EditObserver, EditObserverConfig](this)
    implicit val mutableFragConfig = single[MutableFragment, MutableFragmentConfig](MutableFragmentConfig())
    val photoComposite = singleton(photoModel, DefaultCompositeStrategy(photoModel))
    val proxy = photoComposite.lazyProxy

    implicit val mutStrat = photoComposite.activator(
      ?[BigPhoto] { frag => proxy(!_.isIcon, true)} orElse
        ?[IconPhoto] { frag => proxy(_.isIcon, false)}
    )

    proxy.create
  }
}


object AlbumEditor {

//  def isIcon(photo: Photo): Boolean = {
//    photo.width <= 64 && photo.height <= 64
//  }

  val albumModel = parse[AlbumEditor with AlbumLoader](true)

  def apply(): albumModel.LUB = {
    implicit val albumConfig = frag[AlbumEditor, AlbumEditorConfig](AlbumEditorConfig(64, 64))
    val albumComposite = compose(albumModel, DefaultCompositeStrategy(albumModel))
    albumComposite.make

  }

}

@fragment
trait Jin {

}

@fragment
trait Jang {

}


@dimension
trait Renderer {

  def render(): String

  def methodX(i: Int): Int

  def methodY(i: Int): Int

  def methodZ(i: Int): Int

  def methodU(i: Int): Int

  def methodV(i: Int): Int
}

@fragment
trait EntityB {
  var id: Int = _
}

@fragment
trait EntityBRenderer extends Renderer {
  this: EntityB =>

  def render(): String = {
    s"$id"
  }

  var methodXCounterInRenderer = 0
  var methodYCounterInRenderer = 0
  var methodZCounterInRenderer = 0
  var methodUCounterInRenderer = 0
  var methodVCounterInRenderer = 0

  def methodX(i: Int) = {
    methodXCounterInRenderer += 1
    2 * i
  }

  override def methodY(i: Int): Int = {
    methodYCounterInRenderer += 1
    2 * i
  }

  override def methodZ(i: Int): Int = {
    methodZCounterInRenderer += 1
    2 * i
  }

  override def methodU(i: Int): Int = {
    methodUCounterInRenderer += 1
    2 * i
  }

  override def methodV(i: Int): Int = {
    methodVCounterInRenderer += 1
    2 * i
  }

}

@wrapper @dimension
trait XMLRenderer extends Renderer {

  var xmlRendererCounter = 0

  var methodXCounterInXMLRenderer = 0
  var methodYCounterInXMLRenderer = 0
  var methodUCounterInXMLRenderer = 0

  abstract override def render(): String = {
    xmlRendererCounter += 1
    s"<${super.render()}/>"
  }

  abstract override def methodX(i: Int): Int = {
    methodXCounterInXMLRenderer += 1
    super.methodX(i) * 3
  }

  abstract override def methodY(i: Int): Int = {
    methodYCounterInXMLRenderer += 1
    super.methodY(i) * 3
  }

  override def methodU(i: Int): Int = {
    methodUCounterInXMLRenderer += 1
    super.methodX(i)
  }
}

//todo: Check that a wrapper really wraps a fragment or another compatible wrapper
@wrapper @fragment
trait EntityBRendererDecorator extends EntityBRenderer {
  this: EntityB =>

  var entityBRendererDecoratorCounter = 0

  var methodXCounterInDecorator = 0
  var methodZCounterInDecorator = 0
  var methodVCounterInDecorator = 0

  override def render(): String = {
    entityBRendererDecoratorCounter += 1
    s"{${super.render()}}"
  }

  override def methodX(i: Int): Int = {
    methodXCounterInDecorator += 1
    super.methodX(i) * 4
  }

  override def methodZ(i: Int): Int = {
    methodZCounterInDecorator += 1
    super.methodZ(i) * 4
  }

  override def methodV(i: Int): Int = {
    methodVCounterInDecorator += 1
    super.methodU(i)
  }
}

@dimension
trait Stateful {
  def switch(): Unit
}

@fragment
trait StatefulX extends Stateful {

  this: MutableFragment =>

  override def switch() {
    fire("status", "y", this)
  }

}

@fragment
trait StatefulY extends Stateful {

  this: MutableFragment =>

  override def switch() {
    fire("status", "x", this)
  }

}