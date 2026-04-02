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
}