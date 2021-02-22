package PercyThePerceptron

import Chisel._
import chisel3.RequireAsyncReset

class percy(bitwidth: Int, nodes: Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val bias = Input(UInt(bitwidth.W))
    val weights = Input(Vec(nodes,UInt(bitwidth.W)))
    val values = Input(Vec(nodes,UInt(bitwidth.W)))
    val out_data = Output(UInt(bitwidth.W))
  })

  val percy = Module(new PerceptronBase(bitwidth,nodes)).io
  percy.values := io.values
  percy.bias := io.bias
  percy.weights := io.weights
  io.out_data := percy.out_data
}

object percy extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new percy(bitwidth = 10, nodes = 6), Array("--target-dir", "generated"))
}