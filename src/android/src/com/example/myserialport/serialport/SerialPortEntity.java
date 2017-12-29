package com.example.myserialport.serialport;

import java.io.File;
import java.io.Serializable;

public class SerialPortEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public File device;
	public int baudrate;
	public int flags;

        public SerialPortEntity() {
	}

        public SerialPortEntity(File device, int baudrate, int flags) {
		super();
		this.device = device;
		this.baudrate = baudrate;
		this.flags = flags;
	}

}
