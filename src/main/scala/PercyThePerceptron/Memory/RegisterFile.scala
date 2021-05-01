package PercyThePerceptron.Memory

import Chisel.{Input, Module, Output, UInt, Vec}
import chisel3._

class RegisterFile(address_bit_width : Int, bit_width : Int, row_count : Int, sub_cell_count : Int) extends Module with RequireAsyncReset {
  val io = IO(new Bundle {
    val address = Input(UInt(address_bit_width.W))
    val write_ena_user = Input(UInt(1.W))
    val read = Output(Vec(sub_cell_count, UInt(bit_width.W)))
    val store = Input(Vec(sub_cell_count, UInt(bit_width.W)))
  })
  val file = Module(new File(bit_width = bit_width, row_count = row_count,sub_cell_count = sub_cell_count)).io
  val controller = Module(new Controller(bit_width = bit_width, row_count = row_count,sub_cell_count = sub_cell_count, address_bit_width = address_bit_width)).io

  controller.address := io.address
  controller.write_ena_user := io.write_ena_user
  controller.store := io.store
  io.read := controller.read

  file.features_to_store := controller.features_to_store
  controller.features_from_store := file.features_from_store
  file.write_ena := controller.write_ena
}

