# Run the connector

## Docker image
There are two ways to run the e-CODEX connector from a Docker container:

* get the latest built image from our JFrog repository
* build your own image from source code

```
The following section assumes that you are familiar with Docker and have it installed on your development machine. If not, proceed to install it.
```

### Already built image
A built image of the connector is available here: [here](https://scm.ecodex.eu/docker/connector-client:latest).

Pull the image by running:

```shell
docker pull https://scm.ecodex.eu/docker/connector-client:latest
```

### Build your own image
By default, there is a ```Dockerfile``` in the root of this project.

Build the connector image by running:

```shell
docker build -t connector:latest .
```

## Run the docker container
### Run the standalone container
#### DBMS installation and configuration
By default, the connector-client uses an embedded H2 database. There is no need to install another DBMS.

#### Connector configuration
To make the connector container work during its launch, we need

* attach some environment variable for configuring its datasource
* as the configuration properties, logs, configuration & output folder and keystore are connector agnostic, we need to bind these configs folders to the container.

To do this, create a working directory and name it as you wish. In our example, we called this directory `container-client`.

```shell
mkdir container-client
```

Create a subfolder within the `container-client` folder

```shell
mkdir -p database messages logs config
```

Place sample configurations in the `config` folder. Examples can be found in the connector source code.

* A `Keystore` sample can be found at:

```
domibusConnectorDistribution/target/domibusConnector/standalone/config
```
Copy `keystore` folder and paste it into the `container/config` folder.

* `connector-client.properties` sample can be found at:

```
client-application/target/connector-client-standalone/config
```
Copy `connector-client.properties` file and paste it into `container-client/config` folder.

* `log4j2.xml` sample can be found at:

```
client-application/target/connector-client-standalone/config
```
Copy `log4j2.xml` file and paste it into `container-client/config` folder.

Grant other users write access to the `container-client` folder by running.

```shell
chmod -R 777 container-client
```

Move to the `container-client` folder.

```shell
cd /path/to/container_client_folder
```

Now we are ready to run our container.

```shell
docker run --name connector-client -d -v ./logs:/app/logs -v ./database:/app/database -v ./messages:/app/messages -v ./config:/app/config connector-client:latest
```
