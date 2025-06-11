# endyear-project-2425
#### newbiquitous-octo-squad
## Cubes Game

This project focuses on implementing a **host transfer mechanism** in a client-server network, enabling the server role to be dynamically reassigned from one client to another during runtime. This is especially useful in peer-to-peer (P2P) systems, multiplayer games, or collaborative applications where continuous availability is critical, even if the original host disconnects or fails.

The system allows:

- A designated server (host) to manage and coordinate connected clients.
- Automatic or manual transfer of the host role to another client.
- State synchronization to ensure the new host has the most recent and accurate data.
- Seamless reconnection of other clients to the new host with minimal disruption.

## Key Features

- **Host Handoff Protocol:** A reliable protocol to initiate and confirm host transfer.
- **Server State Serialization:** Captures the current server state and transfers it to the new host.
- **Failure Recovery:** Handles abrupt host disconnections by assigning a new host.
- **Network Role Switching:** Promotes a client to a server without restarting the entire network.

## Use Cases

- Multiplayer games where the player hosting the session leaves
- Decentralized systems needing failover support

## Technologies Used

- Java sockets (TCP)
- Object serialization for state transfer
- Multithreading for concurrent connections
- IP/port reconfiguration for reconnection

