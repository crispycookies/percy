/*
 * 
 * A UART is a serial port, also called an RS232 interface.
 * 
 */

package PercyThePerceptron

object percy extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Node(bitwidth = 8, nodes = 6), Array("--target-dir", "generated"))
}