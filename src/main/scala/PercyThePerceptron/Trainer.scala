package PercyThePerceptron

import Chisel.{Wire, _}
import chisel3.{AsyncReset, RequireAsyncReset, withReset}


class Trainer (bitwidth: Int, nodes: Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val bias = Input(UInt(bitwidth.W))

    val weights_in = Input(Vec(nodes,UInt(bitwidth.W)))
    val weights_out = Output(Vec(nodes,UInt(bitwidth.W)))

    val train_value = Input(Vec(nodes,UInt(bitwidth.W)))
    val predicted_value = Input(UInt(bitwidth.W))

    val eta = Input(UInt(bitwidth.W))
  })
  // MANY THINGS STILL TO DO
  val f_prediction = RegNext(io.predicted_value)
  val train_value = RegNext(io.train_value)
  val bias = RegNext(io.bias, init=0.U)
  val update = Wire(UInt(bitwidth.W))
  val eta = RegNext(io.eta, init=0.U)

  update := eta * (update - f_prediction)

  for(w <- 0 to nodes){
    val former = Wire(UInt(bitwidth.W))
    val former_with_update = Wire(UInt(bitwidth.W))
    val data = Wire(UInt(bitwidth.W))
    former := io.weights_in;
    data := io.train_value(w);
    former_with_update := former + update * data
    io.weights_out(w) := former_with_update
  }
  bias := update
}
