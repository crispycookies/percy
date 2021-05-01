package PercyThePerceptron

import Chisel._
import PercyThePerceptron.Memory.{Cell, File}
import chisel3.RequireAsyncReset
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

class percy(bitwidth: Int, nodes: Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val bias = Input(UInt(bitwidth.W))
    val weights = Input(Vec(nodes,UInt(bitwidth.W)))
    val values = Input(Vec(nodes,UInt(bitwidth.W)))
    val out_data = Output(UInt(bitwidth.W))
  })

  val percy = Module(new Perceptron(bitwidth,nodes)).io
  percy.values := io.values
  percy.bias := io.bias
  percy.weights := io.weights
  io.out_data := percy.out_data
}

object percy extends App {
  (new chisel3.stage.ChiselStage).execute(
    Array("-X", "verilog"),
    Seq(ChiselGeneratorAnnotation(() =>new File(bit_width = 8, row_count = 5, sub_cell_count = 3))))
}