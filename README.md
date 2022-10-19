Checkout CGX4.0 BSP per procecures

In the layers directory, checkout:
  https://github.com/MontaVista-OpenSourceTechnology/meta-apacheconf.git

In project/conf/bblayers, add:
  ${TOPDIR}/../layers/meta-apacheconf \
to the BBLAYERS variable.

Add the following to project/conf/local.conf:
  DISTRO_FEATURES:append = " acl xattr pam selinux"
  PREFERRED_PROVIDER_virtual/refpolicy ?= "refpolicy-apacheconf"
  DEFAULT_ENFORCING="permissive"

In the project directory, run:
  bitbake core-image-selinux-apacheconf

To run the VM, do:
qemu-system-x86_64 --machine q35 -m 1G -enable-kvm -drive file=tmp/deploy/images/x86-generic-64/core-image-selinux-apacheconf-x86-generic-64.ext4,format=raw -nographic -net nic,model=e1000,macaddr=52:54:00:12:34:61 -net user,hostfwd=tcp::5556-10.0.2.15:22,hostfwd=tcp::8080-10.0.2.15:80 -kernel tmp/deploy/images/x86-generic-64/bzImage-x86-generic-64.bin -append "root=/dev/sda console=ttyS0,115200 selinux=1 enforcing=0"

/etc/network/interfaces should have:
  auto eth0
  iface eth0 inet dhcp

in it, per the recipe, but you should make sure.  It may need to be
adjusted if your network device isn't eth0.

Edit /etc/syslog-ng/syslog-ng.conf and comment out the lines that have
d_xconsole in them.  You will need to fix the bracing on the "log"
line.  This will eliminate some denials; syslog-ng will try to log to
the X server but the X server is not configured in the policy.

Edit /etc/apache2/httpd.conf and uncomment the line with cgid_module on it.

By default /etc/selinux/config has:
  SELINUX=permissive

Scripts are in /usr/libexec/apache2/modules/cgi-bin.  In that
directory, edit test-cgi, make the first line:
#!/bin/bash

Then make test-cgi executable.

Now reboot.  You will be running with selinux enabled but in
permissive mode, so if an invalid access happens it will be allowed
but an audit log will print.  You should be able to connect your
browser to localhost:8080 to talk to apache in qemu, and you can do:
ssh -p 5556 root@localhost to log in to the VM.

You can pass parameters to the test-cgi script by doing something like:
  http://localhost:8080/cgi-bin/test-cgi?parm1=a&parm2=b

Those parms will show up in the QUERY_STRING environment variable.

Unfortunately, the standard apache policy give wide-ranging access to
thing in /etc and other places.  I found that /var/run/sshd.pid is not
allowed, though.  I have added a program /usr/bin/supsec that accessed
the file /usr/share/super-secret/data that is not allowed.

If you run:
  setenforce 1

it will enable full enforcement and you will get audit logs and
invalid accesses will be denied.