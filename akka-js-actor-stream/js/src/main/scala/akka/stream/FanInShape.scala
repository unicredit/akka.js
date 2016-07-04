// auto-generated by sbt-boilerplate
/**
 * Copyright (C) 2014-2015 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.stream

import scala.annotation.unchecked.uncheckedVariance
import scala.collection.immutable

object FanInShape {
  sealed trait Init[O] {
    def outlet: Outlet[O]
    def inlets: immutable.Seq[Inlet[_]]
    def name: String
  }
  final case class Name[O](override val name: String) extends Init[O] {
    override def outlet: Outlet[O] = Outlet(s"$name.out")
    override def inlets: immutable.Seq[Inlet[_]] = Nil
  }
  final case class Ports[O](override val outlet: Outlet[O], override val inlets: immutable.Seq[Inlet[_]]) extends Init[O] {
    override def name: String = "FanIn"
  }
}

abstract class FanInShape[+O] private (_out: Outlet[O @uncheckedVariance], _registered: Iterator[Inlet[_]], _name: String) extends Shape {
  import FanInShape._

  def this(init: FanInShape.Init[O]) = this(init.outlet, init.inlets.iterator, init.name)

  final def out: Outlet[O @uncheckedVariance] = _out
  final override def outlets: immutable.Seq[Outlet[_]] = _out :: Nil
  final override def inlets: immutable.Seq[Inlet[_]] = _inlets

  private var _inlets: Vector[Inlet[_]] = Vector.empty
  protected def newInlet[T](name: String): Inlet[T] = {
    val p = if (_registered.hasNext) _registered.next().asInstanceOf[Inlet[T]] else Inlet[T](s"${_name}.$name")
    _inlets :+= p
    p
  }

  protected def construct(init: Init[O @uncheckedVariance]): FanInShape[O]

  def deepCopy(): FanInShape[O] = construct(Ports[O](_out.carbonCopy(), inlets.map(_.carbonCopy())))
  final def copyFromPorts(inlets: immutable.Seq[Inlet[_]], outlets: immutable.Seq[Outlet[_]]): FanInShape[O] = {
    require(outlets.size == 1, s"proposed outlets [${outlets.mkString(", ")}] do not fit FanInShape")
    require(inlets.size == _inlets.size, s"proposed inlets [${inlets.mkString(", ")}] do not fit FanInShape")
    construct(Ports[O](outlets.head.asInstanceOf[Outlet[O]], inlets))
  }
}

object UniformFanInShape {
  def apply[I, O](outlet: Outlet[O], inlets: Inlet[I]*): UniformFanInShape[I, O] =
    new UniformFanInShape(inlets.size, FanInShape.Ports(outlet, inlets.toList))
}

class UniformFanInShape[-T, +O](val n: Int, _init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(n: Int) = this(n, FanInShape.Name[O]("UniformFanIn"))
  def this(n: Int, name: String) = this(n, FanInShape.Name[O](name))
  def this(outlet: Outlet[O], inlets: Array[Inlet[T]]) = this(inlets.length, FanInShape.Ports(outlet, inlets.toList))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new UniformFanInShape(n, init)
  override def deepCopy(): UniformFanInShape[T, O] = super.deepCopy().asInstanceOf[UniformFanInShape[T, O]]

  val inSeq: immutable.IndexedSeq[Inlet[T @uncheckedVariance]] = Vector.tabulate(n)(i ⇒ newInlet[T](s"in$i"))
  def in(n: Int): Inlet[T @uncheckedVariance] = inSeq(n)
}

class FanInShape1N[-T0, -T1, +O](val n: Int, _init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(n: Int) = this(n, FanInShape.Name[O]("FanInShape1N"))
  def this(n: Int, name: String) = this(n, FanInShape.Name[O](name))
  def this(outlet: Outlet[O @uncheckedVariance], in0: Inlet[T0 @uncheckedVariance], inlets1: Array[Inlet[T1 @uncheckedVariance]]) = this(inlets1.length, FanInShape.Ports(outlet, in0 :: inlets1.toList))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape1N(n, init)
  override def deepCopy(): FanInShape1N[T0, T1, O] = super.deepCopy().asInstanceOf[FanInShape1N[T0, T1, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1Seq: immutable.IndexedSeq[Inlet[T1 @uncheckedVariance]] = Vector.tabulate(n)(i ⇒ newInlet[T1](s"in${i + 1}"))
  def in(n: Int): Inlet[T1 @uncheckedVariance] = {
    require(n > 0, "n must be > 0")
    in1Seq(n - 1)
  }
}

class FanInShape2[-T0, -T1, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape2(init)
  override def deepCopy(): FanInShape2[T0, T1, O] = super.deepCopy().asInstanceOf[FanInShape2[T0, T1, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
}

class FanInShape3[-T0, -T1, -T2, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape3(init)
  override def deepCopy(): FanInShape3[T0, T1, T2, O] = super.deepCopy().asInstanceOf[FanInShape3[T0, T1, T2, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
}

class FanInShape4[-T0, -T1, -T2, -T3, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape4(init)
  override def deepCopy(): FanInShape4[T0, T1, T2, T3, O] = super.deepCopy().asInstanceOf[FanInShape4[T0, T1, T2, T3, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
}

class FanInShape5[-T0, -T1, -T2, -T3, -T4, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape5(init)
  override def deepCopy(): FanInShape5[T0, T1, T2, T3, T4, O] = super.deepCopy().asInstanceOf[FanInShape5[T0, T1, T2, T3, T4, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
}

class FanInShape6[-T0, -T1, -T2, -T3, -T4, -T5, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape6(init)
  override def deepCopy(): FanInShape6[T0, T1, T2, T3, T4, T5, O] = super.deepCopy().asInstanceOf[FanInShape6[T0, T1, T2, T3, T4, T5, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
}

class FanInShape7[-T0, -T1, -T2, -T3, -T4, -T5, -T6, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape7(init)
  override def deepCopy(): FanInShape7[T0, T1, T2, T3, T4, T5, T6, O] = super.deepCopy().asInstanceOf[FanInShape7[T0, T1, T2, T3, T4, T5, T6, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
}

class FanInShape8[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape8(init)
  override def deepCopy(): FanInShape8[T0, T1, T2, T3, T4, T5, T6, T7, O] = super.deepCopy().asInstanceOf[FanInShape8[T0, T1, T2, T3, T4, T5, T6, T7, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
}

class FanInShape9[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape9(init)
  override def deepCopy(): FanInShape9[T0, T1, T2, T3, T4, T5, T6, T7, T8, O] = super.deepCopy().asInstanceOf[FanInShape9[T0, T1, T2, T3, T4, T5, T6, T7, T8, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
}

class FanInShape10[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape10(init)
  override def deepCopy(): FanInShape10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, O] = super.deepCopy().asInstanceOf[FanInShape10[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
}

class FanInShape11[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape11(init)
  override def deepCopy(): FanInShape11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O] = super.deepCopy().asInstanceOf[FanInShape11[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
}

class FanInShape12[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape12(init)
  override def deepCopy(): FanInShape12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O] = super.deepCopy().asInstanceOf[FanInShape12[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
}

class FanInShape13[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape13(init)
  override def deepCopy(): FanInShape13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O] = super.deepCopy().asInstanceOf[FanInShape13[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
}

class FanInShape14[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape14(init)
  override def deepCopy(): FanInShape14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, O] = super.deepCopy().asInstanceOf[FanInShape14[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
}

class FanInShape15[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape15(init)
  override def deepCopy(): FanInShape15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, O] = super.deepCopy().asInstanceOf[FanInShape15[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
}

class FanInShape16[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape16(init)
  override def deepCopy(): FanInShape16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, O] = super.deepCopy().asInstanceOf[FanInShape16[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
}

class FanInShape17[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape17(init)
  override def deepCopy(): FanInShape17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, O] = super.deepCopy().asInstanceOf[FanInShape17[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
}

class FanInShape18[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, -T17, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], in17: Inlet[T17], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: in17 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape18(init)
  override def deepCopy(): FanInShape18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, O] = super.deepCopy().asInstanceOf[FanInShape18[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
  val in17: Inlet[T17 @uncheckedVariance] = newInlet[T17]("in17")
}

class FanInShape19[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, -T17, -T18, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], in17: Inlet[T17], in18: Inlet[T18], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: in17 :: in18 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape19(init)
  override def deepCopy(): FanInShape19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, O] = super.deepCopy().asInstanceOf[FanInShape19[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
  val in17: Inlet[T17 @uncheckedVariance] = newInlet[T17]("in17")
  val in18: Inlet[T18 @uncheckedVariance] = newInlet[T18]("in18")
}

class FanInShape20[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, -T17, -T18, -T19, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], in17: Inlet[T17], in18: Inlet[T18], in19: Inlet[T19], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: in17 :: in18 :: in19 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape20(init)
  override def deepCopy(): FanInShape20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, O] = super.deepCopy().asInstanceOf[FanInShape20[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
  val in17: Inlet[T17 @uncheckedVariance] = newInlet[T17]("in17")
  val in18: Inlet[T18 @uncheckedVariance] = newInlet[T18]("in18")
  val in19: Inlet[T19 @uncheckedVariance] = newInlet[T19]("in19")
}

class FanInShape21[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, -T17, -T18, -T19, -T20, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], in17: Inlet[T17], in18: Inlet[T18], in19: Inlet[T19], in20: Inlet[T20], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: in17 :: in18 :: in19 :: in20 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape21(init)
  override def deepCopy(): FanInShape21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, O] = super.deepCopy().asInstanceOf[FanInShape21[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
  val in17: Inlet[T17 @uncheckedVariance] = newInlet[T17]("in17")
  val in18: Inlet[T18 @uncheckedVariance] = newInlet[T18]("in18")
  val in19: Inlet[T19 @uncheckedVariance] = newInlet[T19]("in19")
  val in20: Inlet[T20 @uncheckedVariance] = newInlet[T20]("in20")
}

class FanInShape22[-T0, -T1, -T2, -T3, -T4, -T5, -T6, -T7, -T8, -T9, -T10, -T11, -T12, -T13, -T14, -T15, -T16, -T17, -T18, -T19, -T20, -T21, +O](_init: FanInShape.Init[O]) extends FanInShape[O](_init) {
  def this(name: String) = this(FanInShape.Name[O](name))
  def this(in0: Inlet[T0], in1: Inlet[T1], in2: Inlet[T2], in3: Inlet[T3], in4: Inlet[T4], in5: Inlet[T5], in6: Inlet[T6], in7: Inlet[T7], in8: Inlet[T8], in9: Inlet[T9], in10: Inlet[T10], in11: Inlet[T11], in12: Inlet[T12], in13: Inlet[T13], in14: Inlet[T14], in15: Inlet[T15], in16: Inlet[T16], in17: Inlet[T17], in18: Inlet[T18], in19: Inlet[T19], in20: Inlet[T20], in21: Inlet[T21], out: Outlet[O]) = this(FanInShape.Ports(out, in0 :: in1 :: in2 :: in3 :: in4 :: in5 :: in6 :: in7 :: in8 :: in9 :: in10 :: in11 :: in12 :: in13 :: in14 :: in15 :: in16 :: in17 :: in18 :: in19 :: in20 :: in21 :: Nil))
  override protected def construct(init: FanInShape.Init[O @uncheckedVariance]): FanInShape[O] = new FanInShape22(init)
  override def deepCopy(): FanInShape22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, O] = super.deepCopy().asInstanceOf[FanInShape22[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, O]]

  val in0: Inlet[T0 @uncheckedVariance] = newInlet[T0]("in0")
  val in1: Inlet[T1 @uncheckedVariance] = newInlet[T1]("in1")
  val in2: Inlet[T2 @uncheckedVariance] = newInlet[T2]("in2")
  val in3: Inlet[T3 @uncheckedVariance] = newInlet[T3]("in3")
  val in4: Inlet[T4 @uncheckedVariance] = newInlet[T4]("in4")
  val in5: Inlet[T5 @uncheckedVariance] = newInlet[T5]("in5")
  val in6: Inlet[T6 @uncheckedVariance] = newInlet[T6]("in6")
  val in7: Inlet[T7 @uncheckedVariance] = newInlet[T7]("in7")
  val in8: Inlet[T8 @uncheckedVariance] = newInlet[T8]("in8")
  val in9: Inlet[T9 @uncheckedVariance] = newInlet[T9]("in9")
  val in10: Inlet[T10 @uncheckedVariance] = newInlet[T10]("in10")
  val in11: Inlet[T11 @uncheckedVariance] = newInlet[T11]("in11")
  val in12: Inlet[T12 @uncheckedVariance] = newInlet[T12]("in12")
  val in13: Inlet[T13 @uncheckedVariance] = newInlet[T13]("in13")
  val in14: Inlet[T14 @uncheckedVariance] = newInlet[T14]("in14")
  val in15: Inlet[T15 @uncheckedVariance] = newInlet[T15]("in15")
  val in16: Inlet[T16 @uncheckedVariance] = newInlet[T16]("in16")
  val in17: Inlet[T17 @uncheckedVariance] = newInlet[T17]("in17")
  val in18: Inlet[T18 @uncheckedVariance] = newInlet[T18]("in18")
  val in19: Inlet[T19 @uncheckedVariance] = newInlet[T19]("in19")
  val in20: Inlet[T20 @uncheckedVariance] = newInlet[T20]("in20")
  val in21: Inlet[T21 @uncheckedVariance] = newInlet[T21]("in21")
}
