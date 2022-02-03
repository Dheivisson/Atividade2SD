/*
 * Servidor.java
 *
 * Created on 17 de Maio de 2006, 11:27
 *
 * Servidor ECHO: fica em aguardo de solicitação de algum cliente. Quando recebe
 * simplesmente devolve a mensagem.
 */

import java.net.*;
import java.io.*;

public class Servidor {
	public static void main(String args[]) {
		DatagramSocket s = null;
		try {
			s = new DatagramSocket(6789); // cria um socket UDP
			byte[] buffer = new byte[1000];
			while (true) {
				System.out.println("*** Servidor aguardando request");
				// cria datagrama para recepcionar solicitação do cliente
				DatagramPacket req = new DatagramPacket(buffer, buffer.length);
				s.receive(req);
				System.out.println("*** Request recebido de: " + req.getSocketAddress());


				//*****************************************************************************
				String gab =  "1;4;vvvf";  
				String[] vet_dividida = gab.split(";");
				// String x = "1;4;vvvf";

				System.out.println("Questão recebida: " + new String(req.getData(), 0, req.getLength()));
				String quest =  new String(req.getData());
				String[] dividida = quest.split(";");
				String n_quest = dividida[0];
				String n_alter = dividida[1];
				String resposta = dividida[2];
				char[] q = vet_dividida[2].toCharArray();
				char[]r = resposta.toCharArray();
				int acerto = 0;
				int erro = 0;


				if (n_quest.equals(vet_dividida[0])) {

					for(int i = 0; i< q.length; i++) {

						if(r[i] == q[i]) {
							acerto++;
						}
						else
							erro++;

					}

				}

				else
					erro++;


				System.out.println("ACERTO(S): " + acerto + " " + "ERRO(S): " + erro);
				String y = new String (n_quest + ";" + acerto + ";" + erro );
				System.out.println(y);

				byte[]by = y.getBytes();
				req.setData(by);

				//*****************************************************************************


				// envia resposta
				DatagramPacket resp = new DatagramPacket(req.getData(), req.getLength(),
						req.getAddress(), req.getPort());
				s.send(resp);
			}

		} catch (SocketException e) {
			System.out.println("Erro de socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Erro envio/recepcao pacote: " + e.getMessage());         
		} finally {
			if (s != null) s.close();
		}     
	}
}
