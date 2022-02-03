/*
 * Cliente.java
 *
 * Created on 17 de Maio de 2006, 11:10
 *
 * Argumentos: <HostIP> <porta> <mensagem>
 * Ex. java Cliente 127.0.0.1 6789 "mensagem teste"
 * O servidor devolve a msg (echo)
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

class ThreadCliente implements Runnable {
	protected String servidor="localhost";
	protected String msg="";
	protected int porta=6789;
	protected String gabarito;
	ThreadCliente(String args[]) {
		if (args.length > 0) servidor = args[0];
		if (args.length > 1) porta = Integer.parseInt(args[1]);
		if (args.length > 2) msg = args[2];

	}
	public void run() { // a interface Runnable exige a implementação do método run
		Thread thread = Thread.currentThread();
		DatagramSocket s = null;

		try {
			//*****************************************************************************
			//CRIAR SCANNER AQUI!
			System.out.println("**** Digite a Menssagem ***");
			Scanner sc = new Scanner(System.in);
			msg = sc.nextLine();
			//*****************************************************************************
			s = new DatagramSocket(); // cria um socket UDP
			System.out.println("* " + thread.getName() + " * Socket criado na porta: " + s.getLocalPort());
			byte[] m = msg.getBytes(); // transforma arg em bytes
			InetAddress serv = InetAddress.getByName(servidor);



			DatagramPacket req = new DatagramPacket(m, msg.length(), serv, porta); // concatenar aqui!
			s.send(req); // envia datagrama contendo a mensagem m
			System.out.println("* " + thread.getName() + " * Datagrama enviado: " + msg);

			byte[] buffer = new byte[1000];
			DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
			s.setSoTimeout(10000); // timeout em ms
			s.receive(resp); // aguarda resposta do servidor - bloqueante
			System.out.println("* " + thread.getName() + " * Resposta do servidor:" + new String(resp.getData()));

		} catch (SocketException e) {
			// timeout, erro na criação
			System.out.println("* " + thread.getName() + " * Erro socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("* " + thread.getName() + " * Erro envio/recepcao do pacote: " + e.getMessage());
		}  finally {
			if (s != null) s.close();
		} 
	}
}
public class Cliente {
	public static void main(String args[]) {
		Thread cliente1 = new Thread(new ThreadCliente(args), "CLIENTE 1");
		Thread cliente2 = new Thread(new ThreadCliente(args), "CLIENTE 2");
		cliente1.start();
		cliente2.start();

	}

}
