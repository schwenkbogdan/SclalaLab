package types

class IntType extends UserType {

  override def typeName(): String = "Integer"

  override def create(): Any = 0

  override def cloneValue(obj: Any): Any = obj

  override def parseValue(str: String): Any = str.toInt

  override def getComparator(): Comparator = new Comparator {
    override def compare(o1: Any, o2: Any): Int =
      o1.asInstanceOf[Int] - o2.asInstanceOf[Int]
  }
}