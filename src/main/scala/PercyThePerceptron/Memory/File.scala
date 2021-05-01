package PercyThePerceptron.Memory

import Chisel.{Input, Module, Output, UInt, Vec}
import chisel3._

class File(bit_width : Int, row_count : Int, sub_cell_count : Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val features_to_store = Input(Vec(row_count,Vec(sub_cell_count, UInt(bit_width.W))))
    val features_from_store = Output(Vec(row_count, Vec(sub_cell_count, UInt(bit_width.W))))
    val write_ena = Input(Vec(row_count, UInt(1.W)))
  })

  val memory_cells = Array.fill(row_count)(Module(new Cell(bit_width=bit_width, sub_cell_count = sub_cell_count)).io)

  for (i <- 0 until row_count){
    io.features_from_store(i) := memory_cells(i).features_from_store
    memory_cells(i).features_to_store := io.features_to_store(i)
    memory_cells(i).write_ena := io.write_ena(i)
  }
}

