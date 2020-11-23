import io.circe.generic.auto._
import io.circe.parser._
import io.fscala.shopping.shared.Cart
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.ws.{DefaultWSCookie, WSClient}
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

class APISpec extends PlaySpec with ScalaFutures with GuiceOneServerPerSuite {
  val baseURL = s"localhost:$port/v1"
  val productsURL = s"http://$baseURL/products"
  val addProductsURL = s"http://$baseURL/products/add"
  val productsInCartURL = s"http://$baseURL/cart/products"
  def deleteProductInCartURL(productID: String) = s"http://$baseURL/cart/products/$productID"
  def actionProductInCartURL(productID: String, quantity: Int) =
    s"http://$baseURL/cart/products/$productID/quantity/$quantity"

  "The API" should {
    val wsClient = app.injector.instanceOf[WSClient]

    "list all the product" in {
      val response = wsClient.url(productsURL).get().futureValue
      println(response.body)
      response.status mustBe OK
      response.body must include("PEPPER")
      response.body must include("NAO")
      response.body must include("BEOBOT")
    }

    "add a product" in {
      val newProduct =
        """
          |{
          |"name" : "NewOne",
          |"code" : "New",
          |"description" : "The brand new product",
          |"price" : 100.0
          |}
          |""".stripMargin

      val posted = wsClient.url(addProductsURL).post(newProduct).futureValue
      posted.status mustBe OK

      val response = wsClient.url(productsURL).get().futureValue
      println(response.body)
      response.body must include('NewOne')
    }
    "delete a product from the cart" in {
      val productID = "ALD1"
      val quantity = 1
      val posted = wsClient.url(deleteProductInCartURL(productID)).delete().futureValue
      posted.status mustBe OK
    }
    "update a product quantity in the cart" in {
      val productID = "ALD1"
      val quantity = 1
      val posted = wsClient.url(actionProductInCartURL(productID, quantity)).post("").futureValue
      posted.status mustBe OK
      val newQuantity = 99
      val update = wsClient.url(actionProductInCartURL(productID, newQuantity)).put("").futureValue
      update.status mustBe OK
    }

    "add a product in the cart" in {
      val productID = "ALD1"
      val quantity = 1
      val posted = wsClient.url(actionProductInCartURL(productID, quantity)).post("").futureValue
      posted.status mustBe OK
    }
  }
}
