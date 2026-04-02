object Main {
  def main(args: Array[String]): Unit = {

    val list = new CircularList()
    val factory = new UserFactory

    val typeObj = factory.getBuilderByName("Integer")

    list.add(typeObj.parseValue("5"))
    list.add(typeObj.parseValue("2"))
    list.add(typeObj.parseValue("9"))

    println("До сортировки:")
    list.forEach(println)

    list.sortImperative(typeObj.getComparator())

    println("После сортировки:")
    list.forEach(println)
  }
}