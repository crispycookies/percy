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

class PerceptronBase(bitwidth: Int, nodes: Int) extends Module with RequireAsyncReset{
  val io = IO(new Bundle {
    val bias = Input(UInt(bitwidth.W))
    val weights = Input(Vec(nodes,UInt(bitwidth.W)))
    val values = Input(Vec(nodes,UInt(bitwidth.W)))
    val out_data = Output(UInt(bitwidth.W))
  })

  val delay_sync_bias = RegNext(io.bias, init=0.U)

  val nodes_entities = Array.fill(nodes)(Module(new Node(bitwidth)).io)
  val nodes_result = Wire(Vec(nodes,UInt(bitwidth.W)))
  val reduce_results = Wire(UInt(bitwidth.W))
  val reduce_results_added_bias = Wire(UInt(bitwidth.W))

  for (i <- 0 until nodes){
    nodes_entities(i).weight := io.weights(i)
    nodes_entities(i).value := io.values(i)
    nodes_result(i) := nodes_entities(i).out_product
  }

  reduce_results := nodes_result.reduce(_+_)
  reduce_results_added_bias := reduce_results + delay_sync_bias

  io.out_data := reduce_results_added_bias
}