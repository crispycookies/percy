package PercyThePerceptron

import Chisel._
import chisel3.{AsyncReset, RequireAsyncReset, withReset}

class Node(bitwidth : Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val value = Input(UInt(bitwidth.W))
    val weight = Input(UInt(bitwidth.W))
    val out_product = Output(UInt(bitwidth.W))
  })
  val register_value = RegNext(io.value, init=0.U)
  val register_weight = RegNext(io.weight, init=0.U)

  io.out_product := register_value * register_weight
}