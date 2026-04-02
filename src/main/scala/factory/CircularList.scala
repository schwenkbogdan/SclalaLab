package factory

import java.io.{FileInputStream, FileOutputStream, DataInputStream, DataOutputStream,
                BufferedReader, InputStreamReader, PrintWriter}
import types.{UserType, Comparator, IntType, VectorType, Vector2D}
import model.Node

class CircularList {

  private var head: Node = null
  private var tail: Node = null
  private var size = 0

  def add(value: Any): Unit = {
    val newNode = new Node(value)

    if (head == null) {
      head = newNode
      tail = newNode
      tail.next = head
    } else {
      tail.next = newNode
      tail = newNode
      tail.next = head
    }

    size += 1
  }

  def getNode(index: Int): Node = {
    if (index < 0 || index >= size) return null

    var current = head
    for (_ <- 0 until index)
      current = current.next

    current
  }

  def remove(index: Int): Unit = {
    if (size == 0 || index < 0 || index >= size) return

    if (size == 1) {
      head = null
      tail = null
      size = 0
      return
    }

    var current = head
    var prev = tail

    for (_ <- 0 until index) {
      prev = current
      current = current.next
    }

    if (current == head) {
      head = head.next
      tail.next = head
    } else if (current == tail) {
      tail = prev
      tail.next = head
    } else {
      prev.next = current.next
    }

    size -= 1
  }

  def forEach(f: Any => Unit): Unit = {
    var current = head
    for (_ <- 0 until size) {
      f(current.value)
      current = current.next
    }
  }

  def firstThat(predicate: Any => Boolean): Option[Any] = {
    var current = head
    for (_ <- 0 until size) {
      if (predicate(current.value)) {
        return Some(current.value)
      }
      current = current.next
    }
    None
  }

  def getSize: Int = size

  // ---------- СОРТИРОВКА ----------

  def sortFunctional(comp: Comparator): CircularList = {
    val list = toScalaList()
    val sorted = mergeSort(list, comp)

    val newList = new CircularList
    sorted.foreach(newList.add)
    newList
  }

  def sortImperative(comp: Comparator): Unit = {
    val arr = toScalaList().toArray

    for (i <- arr.indices; j <- i + 1 until arr.length) {
      if (comp.compare(arr(i), arr(j)) > 0) {
        val tmp = arr(i)
        arr(i) = arr(j)
        arr(j) = tmp
      }
    }

    var current = head
    for (i <- arr.indices) {
      current.value = arr(i)
      current = current.next
    }
  }

  private def toScalaList(): List[Any] = {
    var result = List[Any]()
    var current = head

    for (_ <- 0 until size) {
      result = result :+ current.value
      current = current.next
    }

    result
  }

  private def mergeSort(list: List[Any], comp: Comparator): List[Any] = {
    if (list.size <= 1) list
    else {
      val (left, right) = list.splitAt(list.size / 2)
      merge(mergeSort(left, comp), mergeSort(right, comp), comp)
    }
  }

  private def merge(a: List[Any], b: List[Any], comp: Comparator): List[Any] = {
    (a, b) match {
      case (Nil, _) => b
      case (_, Nil) => a
      case (x :: xs, y :: ys) =>
        if (comp.compare(x, y) <= 0)
          x :: merge(xs, b, comp)
        else
          y :: merge(a, ys, comp)
    }
  }

  // ---------- ЛЕКСИКОГРАФИЧЕСКАЯ СОРТИРОВКА ----------

  def lexicographicSort(): CircularList = {
    val list = toScalaList()
    val sorted = list.sortBy(_.toString)
    
    val newList = new CircularList
    sorted.foreach(newList.add)
    newList
  }

  // ---------- СЕРИАЛИЗАЦИЯ ----------

  def saveToText(filename: String, userType: UserType): Unit = {
    val out = new PrintWriter(new java.io.FileWriter(filename))
    try {
      forEach(value => out.println(value.toString))
    } finally {
      out.close()
    }
  }

  def loadFromText(filename: String, userType: UserType): CircularList = {
    val list = new CircularList
    val reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))
    try {
      var line = reader.readLine()
      while (line != null) {
        if (line.nonEmpty) {
          val value = userType.parseValue(line)
          list.add(value)
        }
        line = reader.readLine()
      }
    } finally {
      reader.close()
    }
    list
  }

  def saveToBinary(filename: String, userType: UserType): Unit = {
    val out = new DataOutputStream(new FileOutputStream(filename))
    try {
      out.writeInt(getSize)
      forEach(value => {
        value match {
          case i: Int => out.writeInt(i)
          case v: Vector2D =>
            out.writeDouble(v.x)
            out.writeDouble(v.y)
          case _ => // Для других типов можно расширить
        }
      })
    } finally {
      out.close()
    }
  }

  def loadFromBinary(filename: String, userType: UserType): CircularList = {
    val list = new CircularList
    val in = new DataInputStream(new FileInputStream(filename))
    try {
      val count = in.readInt()
      for (_ <- 0 until count) {
        val value = userType match {
          case _: IntType => in.readInt()
          case _: VectorType =>
            val x = in.readDouble()
            val y = in.readDouble()
            Vector2D(x, y)
          case _ => null
        }
        if (value != null) list.add(value)
      }
    } finally {
      in.close()
    }
    list
  }

  // ---------- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ----------

  def toArray: Array[Any] = toScalaList().toArray

  def insert(index: Int, value: Any): Unit = {
    if (index < 0 || index > size) throw new IndexOutOfBoundsException(s"Index: $index, Size: $size")
    
    if (index == size) {
      add(value)
      return
    }
    
    val newNode = new Node(value)
    
    if (index == 0) {
      newNode.next = head
      head = newNode
      tail.next = head
    } else {
      var current = head
      for (_ <- 0 until index - 1) {
        current = current.next
      }
      newNode.next = current.next
      current.next = newNode
    }
    
    size += 1
  }

  def get(index: Int): Any = {
    val node = getNode(index)
    if (node != null) node.value else null
  }
}

object CircularList:
  def apply(): CircularList = new CircularList()