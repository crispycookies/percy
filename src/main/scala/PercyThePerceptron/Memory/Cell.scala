package PercyThePerceptron.Memory

import Chisel._
import chisel3.RequireAsyncReset

class Cell(bit_width : Int, sub_cell_count : Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val features_to_store = Input(Vec(sub_cell_count,UInt(bit_width.W)))
    var features_from_store = Output(Vec(sub_cell_count, UInt(bit_width.W)))
    val write_ena = Input(UInt(1))
  })
  val cntReg = RegInit(Vec(Seq.fill(sub_cell_count)(0.U(bit_width.W))))
  cntReg:=Mux(io.write_ena === 1.U, io.features_to_store, cntReg)
  io.features_from_store := cntReg
}

