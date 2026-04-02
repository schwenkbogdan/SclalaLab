trait UserType {
  def typeName(): String
  def create(): Any
  def cloneValue(obj: Any): Any
  def parseValue(str: String): Any
  def getComparator(): Comparator
}