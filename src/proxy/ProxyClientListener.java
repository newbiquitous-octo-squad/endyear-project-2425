package proxy;

import protocol.Message;

public class ProxyClientListener extends AbstractProxyListener {
    private ClientData server;
    public ProxyClientListener(ClientData clientData, ClientData server) {
        super(clientData);
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                readMessage().ifPresent(message -> send(message, server));
            } catch (Exception e) {
                System.err.println("uh oh");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void send(Message m, ClientData receiver) {
        super.send(m, server);
    }


    @Override
    public void onDisconnect() {
        // TODO: HANDLE CLIENT DISCONNECT
    }
}
