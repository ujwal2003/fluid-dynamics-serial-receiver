# Fluid Dynamics Serial Receiver
Archive of a project to assist in collection of data from fluid samples through a machine.   
Data collection is gathered through electrical wires connected to an arduino, the data is transmitted through a serial port.   

A Java program creates a GUI which allows you to connect to the serial port and gathers the data through the use of `jSerialComm` package.    
Upon completion of the data transmission the program packages it into a CSV file (which can be opened in programs like MS Excel) and stores it into a directory of your choice.