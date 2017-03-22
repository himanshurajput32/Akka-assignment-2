
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class ValidationActorSpec extends TestKit(ActorSystem("test-system")) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "Validation Actor " must {
    "return one piece at a time" in {
      val ref1 = system.actorOf(Props(new PurchaseActor))
      val ref = TestActorRef(Props(classOf[ValidationActor], ref1))
      ref ! (Customer("", "", "", ""), 3)
      expectMsg("One Piece At a time")
    }
  }
  "Validation Actor " must {
    "decrease piece by 1" in {
      val ref1 = system.actorOf(Props(new PurchaseActor))
      val ref = TestActorRef(Props(classOf[ValidationActor], ref1))
      ref ! (Customer("", "", "", ""), 1)
      ref.underlying.av
    }
  }

}
