---
layout: global
title: 在GCE上运行Alluxio
nickname: 在GCE上运行Alluxio
group: Deploying Alluxio
priority: 4
---

* 内容列表
{:toc}

使用Alluxio自带的
[Vagrant scripts](https://github.com/alluxio/alluxio/tree/master/deploy/vagrant)脚本可以将Alluxio部署在Google计算引擎（GCE）上。该脚本允许你创建，配置以及撤销集群。

## 前期准备

**安装Vagrant和Google插件**

下载 [Vagrant](https://www.vagrantup.com/downloads.html)

安装Google Vagrant 插件:

```console
$ vagrant plugin install vagrant-google
$ vagrant box add google https://github.com/mitchellh/vagrant-google/raw/master/google.box
```

**安装 Alluxio**

使用“Clone”将[Alluxio](https://github.com/Alluxio/alluxio)源码复制到你本地的机器上。

**安装python依赖库**

安装 [python>=2.7](https://www.python.org/), 注意不是 python3.

进入 `deploy/vagrant` 目录下，运行:

```console
$ sudo bash bin/install.sh
```

除了上述方法，你还可以手动安装 [pip](https://pip.pypa.io/en/latest/installing/), 之后进入 `deploy/vagrant` 目录，运行:

```console
$ sudo pip install -r pip-req.txt
```

## 启动集群

要在GCE上运行Alluxio集群, 首先在 [Google Cloud](cloud.google.com) 上有一个计费账号，项目，服务账户和JSON服务账号的密钥。

如果你未注册过Google Cloud, 你需要在 [free trial signup page](https://console.cloud.google.com/billing/freetrial)上创建一个计费账号和项目。 同样地，如果你不熟悉Google计算引擎，你可以先在 [documentation](http://cloud.google.com/compute/docs)上浏览关于这部分的内容。

接着, Google Cloud新用户需要在[Service Accounts](http://console.cloud.google.com/permissions)选项卡下的[Permissions](http://console.cloud.google.com/permissions)页面中的[Console](console.google.com)内选择或者创建一个服务帐号。
如果要创建一个新的服务账号, 请在账号创建的对话框中核对 "Furnish a new private key."信息，并下载JSON密钥，将它存储在一个安全的位置。
如果要使用了一个已有的服务账号, 你需要使用这个账号已有的JSON密钥或者重新下载一个新的。 你可以在 [Service Accounts](http://console.cloud.google.com/permissions)选项卡下找到在服务账号列表右侧的三个句点下的菜单，并选择“create key”，为已有的服务账号下载一个新的JSON密钥，并将它存储在一个安全的位置。

使用[gcloud sdk](http://console.cloud.google.com) 配置ssh密钥:

```console
$ curl https://sdk.cloud.google.com | bash
$ exec -l $SHELL
$ gcloud init
$ gcloud compute config-ssh
```

通过复制下面提供的模板来创建Vagrant GCE配置文件:

```console
$ cp deploy/vagrant/conf/gce.yml.template deploy/vagrant/conf/gce.yml
```

在 `deploy/vagrant/conf/gce.yml`配置文件中,设置你的项目id,服务账号, JSON密钥的位置和已经创建好的ssh用户名.

对于GCE来说，默认的底层文件系统是Google Cloud Storage(GCS)。你需要登入你的[Google云控制台](https://console.cloud.google.com)，新建一个GCS bucket并将该bucket的名称写到`conf/ufs.yml`里面的`GCS:Bucket`字段中。要使用其他底层存储系统的话，配置`conf/ufs.yml`里面`Type`字段及其相关字段。

为了使用访问密钥访问GCS，你需要在GCS控制台中的[互操作性设置](https://console.cloud.google.com/storage/settings)里面创建[开发者密钥](https://cloud.google.com/storage/docs/migrating#keys)，并将shell环境变量`GCS_ACCESS_KEY_ID`和`GCS_SECRET_ACCESS_KEY`进行如下设置：

```console
$ export GCS_ACCESS_KEY_ID=<your access key>
$ export GCS_SECRET_ACCESS_KEY=<your secret access key>
```

现在你可以启动Alluxio集群了，通过在 `deploy/vagrant`目录下运行:

```console
$ ./create <number of machines> google
```

集群中的每个节点运行一个Alluxio worker, `AlluxioMaster` 节点上运行Alluxio master。

## 访问集群

**通过Web UI访问**

命令 `./create <number of machines> google` 运行成功后, 在shell中会输出类似下面的两条语句:

```
>>> AlluxioMaster public IP is xxx, visit xxx:19999 for Alluxio web UI
>>> visit default port of the web UI of what you deployed
```

Alluxio Web UI的默认端口为 **19999**.

在访问Web UI之前, 需要配置防火墙以允许19999端口上的tcp传输。
可以通过在 [Console](https://console.cloud.google.com) UI 上完成或者使用类似如下的gcloud命令，假设网络名是 'default'.

```console
$ gcloud compute firewall-rules create alluxio-ui --allow tcp:19999
```

在浏览器中输入 `http://{MASTER_IP}:{PORT}` 地址访问Web UI。

也可以通过
[Google Cloud console](console.cloud.google.com)监视其状态。

这里是一些当你检查控制台时，可能会遇到的问题:
 - 当集群创建失败，请检查 status/logs实例日志。
 - 集群奔溃后，确保 GCE 实例被终止。
 - 当不再使用集群时，确保 GCE 实例不会再占用额外的内存。

**通过ssh访问**

节点的名称依次被设置成 `AlluxioMaster`, `AlluxioWorker1`, `AlluxioWorker2`等等。

通过ssh登陆一个节点，运行：

```console
$ vagrant ssh <node name>
```

例如，通过以下命令可以登陆 `AlluxioMaster`节点:

```console
$ vagrant ssh AlluxioMaster
```

Alluxio安装在 `/alluxio`。

在 `AlluxioMaster` 节点上，可以对Alluxio运行测试检测其健康状态:

```console
$ /alluxio/bin/alluxio runTests
```

在所有测试完成后，再次访问Alluxio的web UI `http://{MASTER_IP}:19999`，在导航栏中点击 `Browse
File System` 你应该能看到测试过程中写入到Alluxio的文件。

在集群中的某个节点上，可以通过ssh免密码登陆到集群中的其他节点：

```console
$ ssh AlluxioWorker1
```

## 撤销集群

在 `deploy/vagrant` 目录下运行：

```console
$ ./destroy
```

从而撤销之前创建的集群。一次只能创建一个集群。当该命令成功执行后，GCE 实例将终止运行。
