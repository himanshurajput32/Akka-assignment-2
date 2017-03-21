import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationDouble
import akka.routing.FromConfig
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import akka.event.Logging
/**
  * Created by knoldus on 21/3/17.
  */
case class Customer(name:String,address:String,creditCard:String,mobile:String)
class PurchaseActor extends Actor{

  val log = Logging(context.system,this)
  override def receive={
    case Customer(name,address,creditCard,mobile)=>{
    log.info(self.path+"    Purchase Successfull" +"   "+"    "+name+"  "+address+"   "+creditCard+"   "+mobile)
    }
  }
}
class PurchaseRequstHandler(validateRef:ActorRef,purchaseRef:ActorRef) extends Actor{

  val log = Logging(context.system,this)
  override def receive={
    case (Customer(name,address,creditCard,mobile),pieces)=>{
        val customer=Customer(name,address,creditCard,mobile)
        implicit val timeout=Timeout(1000 second)
        val res=  validateRef ? (customer,pieces)
         res.foreach(println)
      }
    }
  }

class ValidationActor(purchaseRef:ActorRef) extends Actor{
  val log = Logging(context.system,this)
  var availablePieces=10
  override def receive={
    case (Customer(name,address,creditCard,mobile),pieces)=>{
      if(pieces!=1){
        sender ! "One Piece At a time"
      }

      else if(availablePieces>0){
        val customer=Customer(name,address,creditCard,mobile)
        implicit val timeout=Timeout(1000 second)
        availablePieces -=1
        purchaseRef.forward(customer)
      }
      else{
        log.info(self.path+"    Sold Out")
      }
    }
  }
}

object SamsungGalaxyS8Sale extends App{
  //var availablePieces: Int =1000

  val config = ConfigFactory.parseString(
    """
      |akka.actor.deployment {
      | /poolRouter {
      |   router =balancing-pool
      |   resizer {
      |      pressure-threshold = 0
      |      lower-bound = 2
      |      upper-bound = 1000
      |      messages-per-resize = 1
      |    }
      | }
      |}
    """.stripMargin
  )

  implicit val timeout = Timeout(4000 seconds)
  val system = ActorSystem("SamsungSale", config)
  val purchaseRef=system.actorOf(Props[PurchaseActor])
  val validateRef=system.actorOf(Props(classOf[ValidationActor],purchaseRef))

  val router = system.actorOf(FromConfig.props(Props(classOf[PurchaseRequstHandler],validateRef,purchaseRef)), "poolRouter")
  for(i<-1 to 20){
    router ! (Customer("aaa","aaaa","aaaa","vvvvv"),1)
  }
// for(i<-1 to 1000){
//    router ! (Customer("bbb","bbbb","bbb","vvvvv"),1)
//  }
//  for(i<-1 to 350){
//    router ! (Customer("aaa","aaaa","aaaa","vvvvv"),1)
//  }
}