package messagingpatterns

import scala.collection.Map
import akka.actor._

case class Order(id: String, orderType: String, orderItems: Map[String, OrderItem]) {
  val grandTotal: Double = orderItems.values.map(orderItem => orderItem.price).sum

  override def toString = {
    s"Order($id, $orderType, $orderItems, Totaling: $grandTotal)"
  }
}

case class OrderItem(id: String, itemType: String, description: String, price: Double) {
  override def toString = {
    s"OrderItem($id, $itemType, '$description', $price)"
  }
}

case class OrderPlaced(order: Order)

object ContentBasedRouterDriver extends CompletableApp(3) {
  val orderRouter = system.actorOf(Props[OrderRouter], "orderRouter")
  val orderItem1 = OrderItem("1", "TypeABC.4", "An item of type ABC.4.", 29.95)
  val orderItem2 = OrderItem("2", "TypeABC.1", "An item of type ABC.1.", 99.95)
  val orderItem3 = OrderItem("3", "TypeABC.9", "An item of type ABC.9.", 14.95)
  val orderItemsOfTypeA = Map(orderItem1.itemType -> orderItem1, orderItem2.itemType -> orderItem2, orderItem3.itemType -> orderItem3)
  orderRouter ! OrderPlaced(Order("123", "TypeABC", orderItemsOfTypeA))

  val orderItem4 = OrderItem("4", "TypeXYZ.2", "An item of type XYZ.2.", 74.95)
  val orderItem5 = OrderItem("5", "TypeXYZ.1", "An item of type XYZ.1.", 59.95)
  val orderItem6 = OrderItem("6", "TypeXYZ.7", "An item of type XYZ.7.", 29.95)
  val orderItem7 = OrderItem("7", "TypeXYZ.5", "An item of type XYZ.5.", 9.95)
  val orderItemsOfTypeX = Map(orderItem4.itemType -> orderItem4, orderItem5.itemType -> orderItem5, orderItem6.itemType -> orderItem6, orderItem7.itemType -> orderItem7)
  orderRouter ! OrderPlaced(Order("124", "TypeXYZ", orderItemsOfTypeX))

  awaitCompletion
  println("ContentBasedRouter: is completed.")
}

class OrderRouter extends Actor {
  val inventorySystemA = context.actorOf(Props[InventorySystemA], "inventorySystemA")
  val inventorySystemX = context.actorOf(Props[InventorySystemX], "inventorySystemX")

  def receive = {
    case orderPlaced: OrderPlaced =>
      orderPlaced.order.orderType match {
        case "TypeABC" =>
          println(s"OrderRouter: routing $orderPlaced")
          inventorySystemA ! orderPlaced
        case "TypeXYZ" =>
          println(s"OrderRouter: routing $orderPlaced")
          inventorySystemX ! orderPlaced
      }

      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("OrderRouter: received unexpected message")
  }
}

class InventorySystemA extends Actor {
  def receive = {
    case OrderPlaced(order) =>
      println(s"InventorySystemA: handling $order")
      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("InventorySystemA: received unexpected message")
  }
}

class InventorySystemX extends Actor {
  def receive = {
    case OrderPlaced(order) =>
      println(s"InventorySystemX: handling $order")
      ContentBasedRouterDriver.completedStep()
    case _ =>
      println("InventorySystemX: received unexpected message")
  }
}
class CompletableApp(val steps:Int) extends App {
  val canComplete = new java.util.concurrent.CountDownLatch(1);
  val canStart = new java.util.concurrent.CountDownLatch(1);
  val completion = new java.util.concurrent.CountDownLatch(steps);

  val system = ActorSystem("eaipatterns")

  def awaitCanCompleteNow = canComplete.await

  def awaitCanStartNow = canStart.await

  def awaitCompletion = {
    completion.await
    system.shutdown()
  }

  def canCompleteNow() = canComplete.countDown()

  def canStartNow() = canStart.countDown()

  def completeAll() = {
    while (completion.getCount > 0) {
      completion.countDown()
    }
  }

  def completedStep() = completion.countDown()
}