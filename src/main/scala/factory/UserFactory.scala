package factory

import types.{UserType, VectorType, IntType}

class UserFactory {

  private val types = List(
    new VectorType(),
    new IntType()
  )

  def getTypeNameList(): List[String] =
    types.map(_.typeName())

  def getBuilderByName(name: String): UserType =
    types.find(_.typeName() == name).get
}

object UserFactory:
  def apply(): UserFactory = new UserFactory()