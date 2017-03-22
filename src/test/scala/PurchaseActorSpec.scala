
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

import scala.concurrent.duration.DurationDouble

/**
  * Created by knoldus on 22/3/17.
  */

class PurchaseActorSpec extends TestKit(ActorSystem("test-system")) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "Purchase Actor " must {
    "increase counter" in {
      implicit val timeout = Timeout(1000 seconds)
      val ref = TestActorRef[PurchaseActor]
      ref ? Customer("", "", "", "")
      ref.underlyingActor.piecesSold === 1
    }
  }
}
