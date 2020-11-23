package io.fscala.shopping.controllers

import dao.{CartDao, ProductDao}
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.fscala.shopping.shared.{Cart, Product, ProductInCart}
//import io.swagger.annotations._
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.circe.Circe
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class WebServices @Inject()(cc: ControllerComponents, productDao: ProductDao) extends AbstractController(cc) {
  // ********* CART Controller ************ //
  def listCartProducts() = play.mvc.Results.TODO

  def deleteCartProduct(id: String) = play.mvc.Results.TODO

  def addCartProduct(id: String, quantity: String) = play.mvc.Results.TODO

  def updateCartProduct(id: String, quantity: String) = play.mvc.Results.TODO

  // ************* Product Controller ********** //
  def listProduct() = play.mvc.Results.TODO

  def addProduct() = play.mvc.Results.TODO
}
