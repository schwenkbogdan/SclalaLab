package ui

import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.event.ButtonClicked
import javax.swing.JOptionPane
import factory.{CircularList, UserFactory}
import types.{UserType, IntType, VectorType, Vector2D}

/** Главное окно приложения
  * 
  * Особенности реализации в Scala:
  * - Используется scala.swing обёртка над Java Swing
  * - Обработчики событий через reactions и pattern matching
  * - Разделение UI и логики (работа через UserType интерфейс)
  */
class DataView extends MainFrame {
  
  title = "Scala Lab - Circular List"
  
  // ==================== ДАННЫЕ ====================
  private val factory = UserFactory()
  private var currentList: CircularList = CircularList()
  private var currentType: UserType = factory.getBuilderByName("Integer")
  
  // ==================== КОМПОНЕНТЫ ====================
  
  // Панель выбора типа
  private val typeLabel = new Label("Тип данных:")
  private val typeComboBox = new ComboBox[String](factory.getTypeNameList())
  
  // Панель добавления
  private val addLabel = new Label("Добавить:")
  private val addField = new TextField(20)
  private val addButton = new Button("Добавить")
  
  // Панель операций с индексом
  private val indexLabel = new Label("Индекс:")
  private val indexField = new TextField(5)
  private val valueLabel = new Label("Значение:")
  private val valueField = new TextField(15)
  private val getButton = new Button("Получить")
  private val insertButton = new Button("Вставить")
  private val removeButton = new Button("Удалить")
  
  // Панель сортировок
  private val sortLabel = new Label("Сортировка:")
  private val funcSortButton = new Button("Функциональная")
  private val impSortButton = new Button("Императивная")
  private val lexSortButton = new Button("Лексикографическая")
  
  // Панель файловых операций
  private val fileLabel = new Label("Файл:")
  private val fileField = new TextField(20)
  private val saveTextButton = new Button("Сохранить текст")
  private val loadTextButton = new Button("Загрузить текст")
  private val saveBinButton = new Button("Сохранить бинарный")
  private val loadBinButton = new Button("Загрузить бинарный")
  
  // Панель итераторов
  private val iterLabel = new Label("Итераторы:")
  private val forEachButton = new Button("forEach")
  private val firstThatButton = new Button("firstThat")
  private val clearButton = new Button("Очистить")
  
  // Список для отображения данных
  private val listModel = new scala.collection.mutable.ListBuffer[String]
  private val dataList = new ListView[String](listModel)
  private val scrollPane = new ScrollPane(dataList)
  
  // ==================== СБОРКА ИНТЕРФЕЙСА ====================
  
  val mainPanel = new BorderPanel {
    
    // Верхняя панель - выбор типа
    val typePanel = new BoxPanel(Orientation.Horizontal) {
      contents += typeLabel
      contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
      contents += typeComboBox
    }
    layout(typePanel) = North
    
    // Центральная панель - список
    layout(scrollPane) = Center
    
    // Нижняя панель - управление
    val bottomPanel = new BoxPanel(Orientation.Vertical) {
      
      // Панель добавления
      val addPanel = new BoxPanel(Orientation.Horizontal) {
        contents += addLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += addField
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += addButton
      }
      
      // Панель операций с индексом
      val indexPanel = new BoxPanel(Orientation.Horizontal) {
        contents += indexLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += indexField
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += valueLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += valueField
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += getButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += insertButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += removeButton
      }
      
      // Панель сортировок
      val sortPanel = new BoxPanel(Orientation.Horizontal) {
        contents += sortLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += funcSortButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += impSortButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += lexSortButton
      }
      
      // Панель файловых операций
      val filePanel = new BoxPanel(Orientation.Horizontal) {
        contents += fileLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += fileField
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += saveTextButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += loadTextButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += saveBinButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += loadBinButton
      }
      
      // Панель итераторов
      val iterPanel = new BoxPanel(Orientation.Horizontal) {
        contents += iterLabel
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 10) }
        contents += forEachButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += firstThatButton
        contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(5, 10) }
        contents += clearButton
      }
      
      contents += addPanel
      contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 5) }
      contents += indexPanel
      contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 5) }
      contents += sortPanel
      contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 5) }
      contents += filePanel
      contents += new BoxPanel(Orientation.Horizontal) { preferredSize = new Dimension(10, 5) }
      contents += iterPanel
    }
    
    layout(bottomPanel) = South
  }
  
  contents = mainPanel
  
  size = new Dimension(800, 600)
  centerOnScreen()
  
  // ==================== ОБРАБОТЧИКИ СОБЫТИЙ ====================
  
  listenTo(
    addButton, getButton, insertButton, removeButton,
    funcSortButton, impSortButton, lexSortButton,
    saveTextButton, loadTextButton, saveBinButton, loadBinButton,
    forEachButton, firstThatButton, clearButton
  )
  
  reactions += {
    // === ДОБАВЛЕНИЕ ===
    case ButtonClicked(`addButton`) =>
      if addField.text.nonEmpty then
        try
          val value = currentType.parseValue(addField.text.trim)
          currentList.add(value)
          updateDisplay()
          addField.text = ""
        catch
          case e: Exception =>
            JOptionPane.showMessageDialog(null,
              s"Ошибка: ${e.getMessage}",
              "Ошибка добавления",
              JOptionPane.ERROR_MESSAGE)
    
    // === ПОЛУЧИТЬ ===
    case ButtonClicked(`getButton`) =>
      try
        val idx = indexField.text.toInt
        val value = currentList.get(idx)
        if value != null then
          valueField.text = value.toString
          JOptionPane.showMessageDialog(null, s"Значение: $value", "Результат",
            JOptionPane.INFORMATION_MESSAGE)
        else
          JOptionPane.showMessageDialog(null, "Элемент не найден", "Ошибка",
            JOptionPane.ERROR_MESSAGE)
      catch
        case _: NumberFormatException =>
          JOptionPane.showMessageDialog(null, "Неверный формат индекса",
            "Ошибка", JOptionPane.ERROR_MESSAGE)
        case e: IndexOutOfBoundsException =>
          JOptionPane.showMessageDialog(null, s"Индекс вне диапазона: ${e.getMessage}",
            "Ошибка", JOptionPane.ERROR_MESSAGE)
    
    // === ВСТАВИТЬ ===
    case ButtonClicked(`insertButton`) =>
      try
        val idx = indexField.text.toInt
        val value = currentType.parseValue(valueField.text.trim)
        currentList.insert(idx, value)
        updateDisplay()
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка вставки", JOptionPane.ERROR_MESSAGE)
    

    case ButtonClicked(`removeButton`) =>
      try
        val idx = indexField.text.toInt
        currentList.remove(idx)
        updateDisplay()
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка удаления", JOptionPane.ERROR_MESSAGE)
    
    // === ФУНКЦИОНАЛЬНАЯ СОРТИРОВКА ===
    case ButtonClicked(`funcSortButton`) =>
      currentList = currentList.sortFunctional(currentType.getComparator())
      updateDisplay()
    
    // === ИМПЕРАТИВНАЯ СОРТИРОВКА ===
    case ButtonClicked(`impSortButton`) =>
      currentList.sortImperative(currentType.getComparator())
      updateDisplay()
    
    // === ЛЕКСИКОГРАФИЧЕСКАЯ СОРТИРОВКА ===
    case ButtonClicked(`lexSortButton`) =>
      currentList = currentList.lexicographicSort()
      updateDisplay()
    
    // === СОХРАНИТЬ В ТЕКСТ ===
    case ButtonClicked(`saveTextButton`) =>
      try
        currentList.saveToText(fileField.text.trim, currentType)
        JOptionPane.showMessageDialog(null, "Сохранено успешно", "Успех",
          JOptionPane.INFORMATION_MESSAGE)
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка сохранения", JOptionPane.ERROR_MESSAGE)
    
    // === ЗАГРУЗИТЬ ИЗ ТЕКСТА ===
    case ButtonClicked(`loadTextButton`) =>
      try
        currentList = currentList.loadFromText(fileField.text.trim, currentType)
        updateDisplay()
        JOptionPane.showMessageDialog(null, "Загружено успешно", "Успех",
          JOptionPane.INFORMATION_MESSAGE)
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка загрузки", JOptionPane.ERROR_MESSAGE)
    
    // === СОХРАНИТЬ В БИНАРНЫЙ ===
    case ButtonClicked(`saveBinButton`) =>
      try
        currentList.saveToBinary(fileField.text.trim, currentType)
        JOptionPane.showMessageDialog(null, "Сохранено успешно", "Успех",
          JOptionPane.INFORMATION_MESSAGE)
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка сохранения", JOptionPane.ERROR_MESSAGE)
    
    // === ЗАГРУЗИТЬ ИЗ БИНАРНОГО ===
    case ButtonClicked(`loadBinButton`) =>
      try
        currentList = currentList.loadFromBinary(fileField.text.trim, currentType)
        updateDisplay()
        JOptionPane.showMessageDialog(null, "Загружено успешно", "Успех",
          JOptionPane.INFORMATION_MESSAGE)
      catch
        case e: Exception =>
          JOptionPane.showMessageDialog(null, s"Ошибка: ${e.getMessage}",
            "Ошибка загрузки", JOptionPane.ERROR_MESSAGE)
    
    // === ITERATOR: FOREACH ===
    case ButtonClicked(`forEachButton`) =>
      println("=== forEach результат ===")
      currentList.forEach(println)
      JOptionPane.showMessageDialog(null, "Результат выведен в консоль", "forEach",
        JOptionPane.INFORMATION_MESSAGE)
    
    // === ITERATOR: FIRSTTHAT ===
    case ButtonClicked(`firstThatButton`) =>
      val found = currentList.firstThat {
        currentType match
          case _: IntType => (x: Any) => x.asInstanceOf[Int] > 0
          case _: VectorType => (x: Any) => x.asInstanceOf[Vector2D].length() > 0
          case _ => (x: Any) => false
      }
      
      found match
        case Some(v) =>
          JOptionPane.showMessageDialog(null, s"Найдено: $v", "firstThat",
            JOptionPane.INFORMATION_MESSAGE)
        case None =>
          JOptionPane.showMessageDialog(null, "Ничего не найдено", "firstThat",
            JOptionPane.INFORMATION_MESSAGE)
    
    // === ОЧИСТИТЬ ===
    case ButtonClicked(`clearButton`) =>
      currentList = CircularList()
      updateDisplay()
  }
  
  // === ВЫБОР ТИПА ДАННЫХ ===
  listenTo(typeComboBox.selection)
  reactions += {
    case scala.swing.event.SelectionChanged(`typeComboBox`) =>
      val selectedName = typeComboBox.selection.item
      currentType = factory.getBuilderByName(selectedName)
      currentList = new CircularList
      updateDisplay()
  }
  
  // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
  
  private def updateDisplay(): Unit = {
    listModel.clear()
    currentList.forEach(value => listModel += value.toString)
  }
  
  // Инициализация поля файла по умолчанию
  fileField.text = "data.txt"
}
