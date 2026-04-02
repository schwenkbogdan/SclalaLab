package types

case class Vector2D(var x: Double, var y: Double) {
  def length(): Double = math.sqrt(x * x + y * y)

  override def toString: String = s"$x;$y"
}