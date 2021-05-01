package PercyThePerceptron.Memory

import Chisel.{Input, Module, Output, UInt, Vec, switch}
import chisel3._
import chisel3.util.is

class Controller(address_bit_width : Int, bit_width : Int, row_count : Int, sub_cell_count : Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val features_to_store = Output(Vec(row_count,Vec(sub_cell_count, UInt(bit_width.W))))
    val features_from_store = Input(Vec(row_count, Vec(sub_cell_count, UInt(bit_width.W))))
    val write_ena = Output(Vec(row_count, UInt(1.W)))

    val address = Input(UInt(address_bit_width.W))
    val write_ena_user = Input(UInt(1.W))
    val read = Output(Vec(sub_cell_count, UInt(bit_width.W)))
    val store = Input(Vec(sub_cell_count, UInt(bit_width.W)))
  })
  io.write_ena := Vec(Seq.fill(row_count)(0.U(1.W)))
  io.read := Vec(Seq.fill(sub_cell_count)(0.U(bit_width.W)))
  io.features_to_store := Vec(Seq.fill(row_count)(Vec(Seq.fill(sub_cell_count)(0.U(bit_width.W)))))

  when(io.address < row_count.U){
    when(io.write_ena_user === 1.U) {
      io.write_ena(io.address) := 1.U
    }
    io.features_to_store(io.address) := io.store
    io.read := io.features_from_store(io.address)
  }
}

