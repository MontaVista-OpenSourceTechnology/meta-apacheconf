DESCRIPTION = "Super secret information"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILES:${PN} += "${datadir}/super-secret ${datadir}/super-secret/data"

do_compile() {
	echo "cat ${datadir}/super-secret/data" >supsec
	echo "Super Secret Data" >data
}

do_install() {
        install -d ${D}${bindir}
        install -d ${D}${datadir}/super-secret
        install -m 0755 supsec ${D}${bindir}
        install -m 0644 data ${D}${datadir}/super-secret
} 