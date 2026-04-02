class VectorType extends UserType {

  override def typeName(): String = "Vector2D"

  override def create(): Any = Vector2D(0, 0)

  override def cloneValue(obj: Any): Any = {
    val v = obj.asInstanceOf[Vector2D]
    Vector2D(v.x, v.y)
  }

  override def parseValue(str: String): Any = {
    val parts = str.split(";")
    Vector2D(parts(0).toDouble, parts(1).toDouble)
  }

  override def getComparator(): Comparator = new Comparator {
    override def compare(o1: Any, o2: Any): Int = {
      val v1 = o1.asInstanceOf[Vector2D]
      val v2 = o2.asInstanceOf[Vector2D]
      v1.length().compareTo(v2.length())
    }
  }
}