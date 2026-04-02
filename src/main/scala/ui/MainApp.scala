package ui

import scala.swing.Swing
import factory.{CircularList, UserFactory}
import types.Vector2D

/** Главная точка входа приложения
  * 
  * Запускает GUI приложение для работы с циклическим списком.
  * Поддерживает работу с различными типами данных через фабрику.
  */
@main def runApp(): Unit =
  Swing.onEDT {
    val frame = new DataView()
    frame.visible = true
  }

/** Консольная демонстрация (для тестирования) */
@main def runDemo(): Unit =
  println("=" * 60)
  println("Scala Lab - Circular List Demonstration")
  println("=" * 60)
  
  val list = CircularList()
  val factory = UserFactory()

  // Тест с Integer
  println("\n--- Тест с Integer ---")
  val intType = factory.getBuilderByName("Integer")
  
  list.add(intType.parseValue("5"))
  list.add(intType.parseValue("2"))
  list.add(intType.parseValue("9"))
  list.add(intType.parseValue("1"))
  list.add(intType.parseValue("7"))

  println("До сортировки:")
  list.forEach(x => print(s"$x  "))
  println()

  println("После функциональной сортировки:")
  val sortedList = list.sortFunctional(intType.getComparator())
  sortedList.forEach(x => print(s"$x  "))
  println()

  println("firstThat (> 3):")
  sortedList.firstThat((x: Any) => x.asInstanceOf[Int] > 3) match
    case Some(found) => println(s"  Найдено: $found")
    case None => println("  Ничего не найдено")

  // Тест с Vector2D
  println("\n--- Тест с Vector2D ---")
  val vectorList = CircularList()
  val vectorType = factory.getBuilderByName("Vector2D")
  
  vectorList.add(vectorType.parseValue("3;4"))
  vectorList.add(vectorType.parseValue("1;2"))
  vectorList.add(vectorType.parseValue("5;12"))
  vectorList.add(vectorType.parseValue("0;1"))

  println("Содержимое (по длине вектора):")
  vectorList.forEach(v => 
    val vec = v.asInstanceOf[Vector2D]
    print(s"$v (len=${vec.length()})  ")
  )
  println()

  println("После сортировки по длине:")
  val sortedVectors = vectorList.sortFunctional(vectorType.getComparator())
  sortedVectors.forEach(v => 
    val vec = v.asInstanceOf[Vector2D]
    print(s"$v (len=${vec.length()})  ")
  )
  println()

  // Тест сериализации
  println("\n--- Тест сериализации ---")
  sortedList.saveToText("test_int.txt", intType)
  println("Сохранено в test_int.txt")
  
  val loadedList = sortedList.loadFromText("test_int.txt", intType)
  println("Загружено из test_int.txt:")
  loadedList.forEach(x => print(s"$x  "))
  println()

  println("\n" + "=" * 60)
  println("ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА")
  println("=" * 60)
