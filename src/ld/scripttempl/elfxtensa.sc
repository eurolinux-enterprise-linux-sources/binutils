#
# Unusual variables checked by this code:
#	NOP - four byte opcode for no-op (defaults to 0)
#	NO_SMALL_DATA - no .sbss/.sbss2/.sdata/.sdata2 sections if not
#		empty.
#	SMALL_DATA_CTOR - .ctors contains small data.
#	SMALL_DATA_DTOR - .dtors contains small data.
#	DATA_ADDR - if end-of-text-plus-one-page isn't right for data start
#	INITIAL_READONLY_SECTIONS - at start of text segment
#	OTHER_READONLY_SECTIONS - other than .text .init .rodata ...
#		(e.g., .PARISC.milli)
#	OTHER_TEXT_SECTIONS - these get put in .text when relocating
#	OTHER_READWRITE_SECTIONS - other than .data .bss .ctors .sdata ...
#		(e.g., .PARISC.global)
#	OTHER_RELRO_SECTIONS - other than .data.rel.ro ...
#		(e.g. PPC32 .fixup, .got[12])
#	OTHER_BSS_SECTIONS - other than .bss .sbss ...
#	ATTRS_SECTIONS - at the end
#	OTHER_SECTIONS - at the end
#	EXECUTABLE_SYMBOLS - symbols that must be defined for an
#		executable (e.g., _DYNAMIC_LINK)
#       TEXT_START_ADDR - the first byte of the text segment, after any
#               headers.
#       TEXT_BASE_ADDRESS - the first byte of the text segment.
#	TEXT_START_SYMBOLS - symbols that appear at the start of the
#		.text section.
#	DATA_START_SYMBOLS - symbols that appear at the start of the
#		.data section.
#	DATA_END_SYMBOLS - symbols that appear at the end of the
#		writeable data sections.
#	OTHER_GOT_SYMBOLS - symbols defined just before .got.
#	OTHER_GOT_SECTIONS - sections just after .got.
#	OTHER_SDATA_SECTIONS - sections just after .sdata.
#	OTHER_BSS_SYMBOLS - symbols that appear at the start of the
#		.bss section besides __bss_start.
#	DATA_PLT - .plt should be in data segment, not text segment.
#	PLT_BEFORE_GOT - .plt just before .got when .plt is in data segement.
#	BSS_PLT - .plt should be in bss segment
#	NO_REL_RELOCS - Don't include .rel.* sections in script
#	NO_RELA_RELOCS - Don't include .rela.* sections in script
#	NON_ALLOC_DYN - Place dynamic sections after data segment.
#	TEXT_DYNAMIC - .dynamic in text segment, not data segment.
#	EMBEDDED - whether this is for an embedded system. 
#	SHLIB_TEXT_START_ADDR - if set, add to SIZEOF_HEADERS to set
#		start address of shared library.
#	INPUT_FILES - INPUT command of files to always include
#	WRITABLE_RODATA - if set, the .rodata section should be writable
#	INIT_START, INIT_END -  statements just before and just after
# 	combination of .init sections.
#	FINI_START, FINI_END - statements just before and just after
# 	combination of .fini sections.
#	STACK_ADDR - start of a .stack section.
#	OTHER_SYMBOLS - symbols to place right at the end of the script.
#	ETEXT_NAME - name of a symbol for the end of the text section,
#		normally etext.
#	SEPARATE_GOTPLT - if set, .got.plt should be separate output section,
#		so that .got can be in the RELRO area.  It should be set to
#		the number of bytes in the beginning of .got.plt which can be
#		in the RELRO area as well.
#	USER_LABEL_PREFIX - prefix to add to user-visible symbols.
#
# When adding sections, do note that the names of some sections are used
# when specifying the start address of the next.
#

#  Many sections come in three flavours.  There is the 'real' section,
#  like ".data".  Then there are the per-procedure or per-variable
#  sections, generated by -ffunction-sections and -fdata-sections in GCC,
#  and useful for --gc-sections, which for a variable "foo" might be
#  ".data.foo".  Then there are the linkonce sections, for which the linker
#  eliminates duplicates, which are named like ".gnu.linkonce.d.foo".
#  The exact correspondences are:
#
#  Section	Linkonce section
#  .text	.gnu.linkonce.t.foo
#  .rodata	.gnu.linkonce.r.foo
#  .data	.gnu.linkonce.d.foo
#  .bss		.gnu.linkonce.b.foo
#  .sdata	.gnu.linkonce.s.foo
#  .sbss	.gnu.linkonce.sb.foo
#  .sdata2	.gnu.linkonce.s2.foo
#  .sbss2	.gnu.linkonce.sb2.foo
#  .debug_info	.gnu.linkonce.wi.foo
#  .tdata	.gnu.linkonce.td.foo
#  .tbss	.gnu.linkonce.tb.foo
#  .lrodata	.gnu.linkonce.lr.foo
#  .ldata	.gnu.linkonce.l.foo
#  .lbss	.gnu.linkonce.lb.foo
#
# plus exception-handling information for Tensilica's XCC compiler:
#  .xt_except_table .gnu.linkonce.e.foo
#  .xt_except_desc  .gnu.linkonce.h.foo
#
# plus Xtensa-specific literal sections:
#  .literal	.gnu.linkonce.literal.foo
#  .lit4   	.gnu.linkonce.lit4.foo
#
# plus Xtensa-specific "property table" sections:
#  .xt.lit      .gnu.linkonce.p.foo
#  .xt.insn     .gnu.linkonce.x.foo (obsolete)
#  .xt.prop     .gnu.linkonce.prop.foo
#
#  Each of these can also have corresponding .rel.* and .rela.* sections.

test -z "$ENTRY" && ENTRY=_start
test -z "${BIG_OUTPUT_FORMAT}" && BIG_OUTPUT_FORMAT=${OUTPUT_FORMAT}
test -z "${LITTLE_OUTPUT_FORMAT}" && LITTLE_OUTPUT_FORMAT=${OUTPUT_FORMAT}
if [ -z "$MACHINE" ]; then OUTPUT_ARCH=${ARCH}; else OUTPUT_ARCH=${ARCH}:${MACHINE}; fi
test -z "${ELFSIZE}" && ELFSIZE=32
test -z "${ALIGNMENT}" && ALIGNMENT="${ELFSIZE} / 8"
test "$LD_FLAG" = "N" && DATA_ADDR=.
test -z "${ETEXT_NAME}" && ETEXT_NAME=etext
test -n "$CREATE_SHLIB$CREATE_PIE" && test -n "$SHLIB_DATA_ADDR" && COMMONPAGESIZE=""
test -z "$CREATE_SHLIB$CREATE_PIE" && test -n "$DATA_ADDR" && COMMONPAGESIZE=""
test -n "$RELRO_NOW" && unset SEPARATE_GOTPLT
test -z "$ATTRS_SECTIONS" && ATTRS_SECTIONS=".gnu.attributes 0 : { KEEP (*(.gnu.attributes)) }"
DATA_SEGMENT_ALIGN="ALIGN(${SEGMENT_SIZE}) + (. & (${MAXPAGESIZE} - 1))"
DATA_SEGMENT_RELRO_END=""
DATA_SEGMENT_END=""
if test -n "${COMMONPAGESIZE}"; then
  DATA_SEGMENT_ALIGN="ALIGN (${SEGMENT_SIZE}) - ((${MAXPAGESIZE} - .) & (${MAXPAGESIZE} - 1)); . = DATA_SEGMENT_ALIGN (${MAXPAGESIZE}, ${COMMONPAGESIZE})"
  DATA_SEGMENT_END=". = DATA_SEGMENT_END (.);"
  DATA_SEGMENT_RELRO_END=". = DATA_SEGMENT_RELRO_END (${SEPARATE_GOTPLT-0}, .);"
fi
if test -z "${INITIAL_READONLY_SECTIONS}${CREATE_SHLIB}"; then
  INITIAL_READONLY_SECTIONS=".interp        : { *(.interp) }"
fi
if test -z "$PLT"; then
  PLT=".plt           : { *(.plt) }"
fi
test -n "${DATA_PLT-${BSS_PLT-text}}" && TEXT_PLT=yes
if test -z "$GOT"; then
  if test -z "$SEPARATE_GOTPLT"; then
    GOT=".got           : { *(.got.plt) *(.got) }"
  else
    GOT=".got           : { *(.got) }"
    GOTPLT=".got.plt       : { *(.got.plt) }"
  fi
fi
DYNAMIC=".dynamic       : { *(.dynamic) }"
RODATA=".rodata        : { *(.rodata${RELOCATING+ .rodata.* .gnu.linkonce.r.*}) }"
DATARELRO=".data.rel.ro : { *(.data.rel.ro.local* .gnu.linkonce.d.rel.ro.local.*) *(.data.rel.ro* .gnu.linkonce.d.rel.ro.*) }"
DISCARDED="/DISCARD/ : { *(.note.GNU-stack) *(.gnu_debuglink)  *(.gnu.lto_*) }"
INIT_LIT=".init.literal  : { *(.init.literal)	}"
INIT=".init          : { *(.init)		}"
FINI_LIT=".fini.literal  : { *(.fini.literal)	}"
FINI=".fini          : { *(.fini)		}"
if test -z "${NO_SMALL_DATA}"; then
  SBSS=".sbss          :
  {
    ${RELOCATING+${SBSS_START_SYMBOLS}}
    ${CREATE_SHLIB+*(.sbss2 .sbss2.* .gnu.linkonce.sb2.*)}
    *(.dynsbss)
    *(.sbss${RELOCATING+ .sbss.* .gnu.linkonce.sb.*})
    *(.scommon)
    ${RELOCATING+${SBSS_END_SYMBOLS}}
  }"
  SBSS2=".sbss2         : { *(.sbss2${RELOCATING+ .sbss2.* .gnu.linkonce.sb2.*}) }"
  SDATA="/* We want the small data sections together, so single-instruction offsets
     can access them all, and initialized data all before uninitialized, so
     we can shorten the on-disk segment size.  */
  .sdata         : 
  {
    ${RELOCATING+${SDATA_START_SYMBOLS}}
    ${CREATE_SHLIB+*(.sdata2 .sdata2.* .gnu.linkonce.s2.*)}
    *(.sdata${RELOCATING+ .sdata.* .gnu.linkonce.s.*})
  }"
  SDATA2=".sdata2        :
  {
    ${RELOCATING+${SDATA2_START_SYMBOLS}}
    *(.sdata2${RELOCATING+ .sdata2.* .gnu.linkonce.s2.*})
  }"
  REL_SDATA=".rel.sdata     : { *(.rel.sdata${RELOCATING+ .rel.sdata.* .rel.gnu.linkonce.s.*}) }
  .rela.sdata    : { *(.rela.sdata${RELOCATING+ .rela.sdata.* .rela.gnu.linkonce.s.*}) }"
  REL_SBSS=".rel.sbss      : { *(.rel.sbss${RELOCATING+ .rel.sbss.* .rel.gnu.linkonce.sb.*}) }
  .rela.sbss     : { *(.rela.sbss${RELOCATING+ .rela.sbss.* .rela.gnu.linkonce.sb.*}) }"
  REL_SDATA2=".rel.sdata2    : { *(.rel.sdata2${RELOCATING+ .rel.sdata2.* .rel.gnu.linkonce.s2.*}) }
  .rela.sdata2   : { *(.rela.sdata2${RELOCATING+ .rela.sdata2.* .rela.gnu.linkonce.s2.*}) }"
  REL_SBSS2=".rel.sbss2     : { *(.rel.sbss2${RELOCATING+ .rel.sbss2.* .rel.gnu.linkonce.sb2.*}) }
  .rela.sbss2    : { *(.rela.sbss2${RELOCATING+ .rela.sbss2.* .rela.gnu.linkonce.sb2.*}) }"
else
  NO_SMALL_DATA=" "
fi
if test -z "${DATA_GOT}"; then
  if test -n "${NO_SMALL_DATA}"; then
    DATA_GOT=" "
  fi
fi
if test -z "${SDATA_GOT}"; then
  if test -z "${NO_SMALL_DATA}"; then
    SDATA_GOT=" "
  fi
fi
test -n "$SEPARATE_GOTPLT" && SEPARATE_GOTPLT=" "
test "${LARGE_SECTIONS}" = "yes" && REL_LARGE="
  .rel.ldata     : { *(.rel.ldata${RELOCATING+ .rel.ldata.* .rel.gnu.linkonce.l.*}) }
  .rela.ldata    : { *(.rela.ldata${RELOCATING+ .rela.ldata.* .rela.gnu.linkonce.l.*}) }
  .rel.lbss      : { *(.rel.lbss${RELOCATING+ .rel.lbss.* .rel.gnu.linkonce.lb.*}) }
  .rela.lbss     : { *(.rela.lbss${RELOCATING+ .rela.lbss.* .rela.gnu.linkonce.lb.*}) }
  .rel.lrodata   : { *(.rel.lrodata${RELOCATING+ .rel.lrodata.* .rel.gnu.linkonce.lr.*}) }
  .rela.lrodata  : { *(.rela.lrodata${RELOCATING+ .rela.lrodata.* .rela.gnu.linkonce.lr.*}) }"
test "${LARGE_SECTIONS}" = "yes" && OTHER_BSS_SECTIONS="
  ${OTHER_BSS_SECTIONS}
  .lbss  :
  {
    *(.dynlbss)
    *(.lbss${RELOCATING+ .lbss.* .gnu.linkonce.lb.*})
    *(LARGE_COMMON)
  }"
test "${LARGE_SECTIONS}" = "yes" && LARGE_SECTIONS="
  .lrodata  ${RELOCATING+ALIGN(${MAXPAGESIZE}) + (. & (${MAXPAGESIZE} - 1))} :
  {
    *(.lrodata${RELOCATING+ .lrodata.* .gnu.linkonce.lr.*})
  }
  .ldata  ${RELOCATING+ALIGN(${MAXPAGESIZE}) + (. & (${MAXPAGESIZE} - 1))} :
  {
    *(.ldata${RELOCATING+ .ldata.* .gnu.linkonce.l.*})
    ${RELOCATING+. = ALIGN(. != 0 ? ${ALIGNMENT} : 1);}
  }"
CTOR=".ctors         : 
  {
    ${CONSTRUCTING+${CTOR_START}}
    /* gcc uses crtbegin.o to find the start of
       the constructors, so we make sure it is
       first.  Because this is a wildcard, it
       doesn't matter if the user does not
       actually link against crtbegin.o; the
       linker won't look for a file to match a
       wildcard.  The wildcard also means that it
       doesn't matter which directory crtbegin.o
       is in.  */

    KEEP (*crtbegin.o(.ctors))
    KEEP (*crtbegin?.o(.ctors))

    /* We don't want to include the .ctor section from
       the crtend.o file until after the sorted ctors.
       The .ctor section from the crtend file contains the
       end of ctors marker and it must be last */

    KEEP (*(EXCLUDE_FILE (*crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .ctors))
    KEEP (*(SORT(.ctors.*)))
    KEEP (*(.ctors))
    ${CONSTRUCTING+${CTOR_END}}
  }"
DTOR=".dtors         :
  {
    ${CONSTRUCTING+${DTOR_START}}
    KEEP (*crtbegin.o(.dtors))
    KEEP (*crtbegin?.o(.dtors))
    KEEP (*(EXCLUDE_FILE (*crtend.o *crtend?.o $OTHER_EXCLUDE_FILES) .dtors))
    KEEP (*(SORT(.dtors.*)))
    KEEP (*(.dtors))
    ${CONSTRUCTING+${DTOR_END}}
  }"
STACK="  .stack        ${RELOCATING+${STACK_ADDR}} :
  {
    ${RELOCATING+_stack = .;}
    *(.stack)
  }"

# if this is for an embedded system, don't add SIZEOF_HEADERS.
if [ -z "$EMBEDDED" ]; then
   test -z "${TEXT_BASE_ADDRESS}" && TEXT_BASE_ADDRESS="${TEXT_START_ADDR} + SIZEOF_HEADERS"
else
   test -z "${TEXT_BASE_ADDRESS}" && TEXT_BASE_ADDRESS="${TEXT_START_ADDR}"
fi

cat <<EOF
${RELOCATING+ENTRY(${ENTRY})}

${RELOCATING+${LIB_SEARCH_DIRS}}
${RELOCATING+${EXECUTABLE_SYMBOLS}}
${RELOCATING+${INPUT_FILES}}

SECTIONS
{
  /* Read-only sections, merged into text segment: */
  ${CREATE_SHLIB-${CREATE_PIE-${RELOCATING+PROVIDE (__executable_start = ${TEXT_START_ADDR}); . = ${TEXT_BASE_ADDRESS};}}}
  ${CREATE_SHLIB+${RELOCATING+. = ${SHLIB_TEXT_START_ADDR:-0} + SIZEOF_HEADERS;}}
  ${CREATE_PIE+${RELOCATING+. = ${SHLIB_TEXT_START_ADDR:-0} + SIZEOF_HEADERS;}}
  ${INITIAL_READONLY_SECTIONS}
  .note.gnu.build-id : { *(.note.gnu.build-id) }
EOF

test -n "${RELOCATING+0}" || unset NON_ALLOC_DYN
test -z "${NON_ALLOC_DYN}" || TEXT_DYNAMIC=
cat > ldscripts/dyntmp.$$ <<EOF
  ${TEXT_DYNAMIC+${DYNAMIC}}
  .hash          : { *(.hash) }
  .gnu.hash      : { *(.gnu.hash) }
  .dynsym        : { *(.dynsym) }
  .dynstr        : { *(.dynstr) }
  .gnu.version   : { *(.gnu.version) }
  .gnu.version_d : { *(.gnu.version_d) }
  .gnu.version_r : { *(.gnu.version_r) }
EOF

if [ "x$COMBRELOC" = x ]; then
  COMBRELOCCAT="cat >> ldscripts/dyntmp.$$"
else
  COMBRELOCCAT="cat > $COMBRELOC"
fi
eval $COMBRELOCCAT <<EOF
  .rel.init      : { *(.rel.init) }
  .rela.init     : { *(.rela.init) }
  .rel.text      : { *(.rel.text${RELOCATING+ .rel.text.* .rel.gnu.linkonce.t.*}) }
  .rela.text     : { *(.rela.text${RELOCATING+ .rela.text.* .rela.gnu.linkonce.t.*}) }
  .rel.fini      : { *(.rel.fini) }
  .rela.fini     : { *(.rela.fini) }
  .rel.rodata    : { *(.rel.rodata${RELOCATING+ .rel.rodata.* .rel.gnu.linkonce.r.*}) }
  .rela.rodata   : { *(.rela.rodata${RELOCATING+ .rela.rodata.* .rela.gnu.linkonce.r.*}) }
  ${OTHER_READONLY_RELOC_SECTIONS}
  .rel.data.rel.ro  : { *(.rel.data.rel.ro${RELOCATING+* .rel.gnu.linkonce.d.rel.ro.*}) }
  .rela.data.rel.ro  : { *(.rela.data.rel.ro${RELOCATING+* .rela.gnu.linkonce.d.rel.ro.*}) }
  .rel.data      : { *(.rel.data${RELOCATING+ .rel.data.* .rel.gnu.linkonce.d.*}) }
  .rela.data     : { *(.rela.data${RELOCATING+ .rela.data.* .rela.gnu.linkonce.d.*}) }
  .rel.tdata	 : { *(.rel.tdata${RELOCATING+ .rel.tdata.* .rel.gnu.linkonce.td.*}) }
  .rela.tdata	 : { *(.rela.tdata${RELOCATING+ .rela.tdata.* .rela.gnu.linkonce.td.*}) }
  .rel.tbss	 : { *(.rel.tbss${RELOCATING+ .rel.tbss.* .rel.gnu.linkonce.tb.*}) }
  .rela.tbss	 : { *(.rela.tbss${RELOCATING+ .rela.tbss.* .rela.gnu.linkonce.tb.*}) }
  .rel.ctors     : { *(.rel.ctors) }
  .rela.ctors    : { *(.rela.ctors) }
  .rel.dtors     : { *(.rel.dtors) }
  .rela.dtors    : { *(.rela.dtors) }
  .rel.got       : { *(.rel.got) }
  .rela.got      : { *(.rela.got) }
  ${OTHER_GOT_RELOC_SECTIONS}
  ${REL_SDATA}
  ${REL_SBSS}
  ${REL_SDATA2}
  ${REL_SBSS2}
  .rel.bss       : { *(.rel.bss${RELOCATING+ .rel.bss.* .rel.gnu.linkonce.b.*}) }
  .rela.bss      : { *(.rela.bss${RELOCATING+ .rela.bss.* .rela.gnu.linkonce.b.*}) }
  ${REL_LARGE}
EOF

if [ -n "$COMBRELOC" ]; then
cat >> ldscripts/dyntmp.$$ <<EOF
  .rel.dyn       :
    {
EOF
sed -e '/^[ 	]*[{}][ 	]*$/d;/:[ 	]*$/d;/\.rela\./d;s/^.*: { *\(.*\)}$/      \1/' $COMBRELOC >> ldscripts/dyntmp.$$
cat >> ldscripts/dyntmp.$$ <<EOF
    }
  .rela.dyn      :
    {
EOF
sed -e '/^[ 	]*[{}][ 	]*$/d;/:[ 	]*$/d;/\.rel\./d;s/^.*: { *\(.*\)}/      \1/' $COMBRELOC >> ldscripts/dyntmp.$$
cat >> ldscripts/dyntmp.$$ <<EOF
    }
EOF
fi

cat >> ldscripts/dyntmp.$$ <<EOF
  .rel.plt       : { *(.rel.plt) }
  .rela.plt      : { *(.rela.plt) }
  ${OTHER_PLT_RELOC_SECTIONS}
EOF

if test -z "${NON_ALLOC_DYN}"; then
  if test -z "${NO_REL_RELOCS}${NO_RELA_RELOCS}"; then
    cat ldscripts/dyntmp.$$
  else
    if test -z "${NO_REL_RELOCS}"; then
      sed -e '/^[ 	]*\.rela\.[^}]*$/,/}/d' -e '/^[ 	]*\.rela\./d' ldscripts/dyntmp.$$
    fi
    if test -z "${NO_RELA_RELOCS}"; then
      sed -e '/^[ 	]*\.rel\.[^}]*$/,/}/d' -e '/^[ 	]*\.rel\./d' ldscripts/dyntmp.$$
    fi
  fi
  rm -f ldscripts/dyntmp.$$
fi

cat <<EOF
  ${RELOCATING-$INIT_LIT}
  ${RELOCATING-$INIT}

  ${TEXT_PLT+${PLT}}
  ${TINY_READONLY_SECTION}
  .text          :
  {
    *(.got.plt* .plt*)

    ${RELOCATING+${INIT_START}}
    ${RELOCATING+KEEP (*(.init.literal))}
    ${RELOCATING+KEEP (*(.init))}
    ${RELOCATING+${INIT_END}}

    ${RELOCATING+${TEXT_START_SYMBOLS}}
    *(.literal .text .stub${RELOCATING+ .literal.* .text.* .gnu.linkonce.literal.* .gnu.linkonce.t.*.literal .gnu.linkonce.t.*})
    /* .gnu.warning sections are handled specially by elf32.em.  */
    *(.gnu.warning)
    ${RELOCATING+${OTHER_TEXT_SECTIONS}}

    ${RELOCATING+${FINI_START}}
    ${RELOCATING+KEEP (*(.fini.literal))}
    ${RELOCATING+KEEP (*(.fini))}
    ${RELOCATING+${FINI_END}}
  } =${NOP-0}

  ${RELOCATING-$FINI_LIT}
  ${RELOCATING-$FINI}

  ${RELOCATING+PROVIDE (__${ETEXT_NAME} = .);}
  ${RELOCATING+PROVIDE (_${ETEXT_NAME} = .);}
  ${RELOCATING+PROVIDE (${ETEXT_NAME} = .);}
  ${WRITABLE_RODATA-${RODATA}}
  .rodata1       : { *(.rodata1) }
  ${CREATE_SHLIB-${SDATA2}}
  ${CREATE_SHLIB-${SBSS2}}
  ${OTHER_READONLY_SECTIONS}
  .eh_frame_hdr : { *(.eh_frame_hdr) }
  .eh_frame      : ONLY_IF_RO { KEEP (*(.eh_frame)) }
  .gcc_except_table  : ONLY_IF_RO { *(.gcc_except_table .gcc_except_table.*) }

  /* Adjust the address for the data segment.  We want to adjust up to
     the same address within the page on the next page up.  */
  ${CREATE_SHLIB-${CREATE_PIE-${RELOCATING+. = ${DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}}
  ${CREATE_SHLIB+${RELOCATING+. = ${SHLIB_DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}
  ${CREATE_PIE+${RELOCATING+. = ${SHLIB_DATA_ADDR-${DATA_SEGMENT_ALIGN}};}}

  /* Exception handling  */
  .eh_frame      : ONLY_IF_RW { KEEP (*(.eh_frame)) }
  .gcc_except_table  : ONLY_IF_RW { *(.gcc_except_table .gcc_except_table.*) }

  /* Thread Local Storage sections  */
  .tdata	 : { *(.tdata${RELOCATING+ .tdata.* .gnu.linkonce.td.*}) }
  .tbss		 : { *(.tbss${RELOCATING+ .tbss.* .gnu.linkonce.tb.*})${RELOCATING+ *(.tcommon)} }

  .preinit_array    :
  {
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__preinit_array_start = .);}}
    KEEP (*(.preinit_array))
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__preinit_array_end = .);}}
  }
  .init_array    :
  {
     ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__init_array_start = .);}}
     KEEP (*(SORT(.init_array.*)))
     KEEP (*(.init_array))
     ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__init_array_end = .);}}
  }
  .fini_array    :
  {
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__fini_array_start = .);}}
    KEEP (*(.fini_array))
    KEEP (*(SORT(.fini_array.*)))
    ${RELOCATING+${CREATE_SHLIB-PROVIDE_HIDDEN (${USER_LABEL_PREFIX}__fini_array_end = .);}}
  }
  ${SMALL_DATA_CTOR-${RELOCATING+${CTOR}}}
  ${SMALL_DATA_DTOR-${RELOCATING+${DTOR}}}
  .jcr           : { KEEP (*(.jcr)) }

  ${RELOCATING+${DATARELRO}}
  ${OTHER_RELRO_SECTIONS}
  ${TEXT_DYNAMIC-${DYNAMIC}}
  ${DATA_GOT+${RELRO_NOW+${GOT}}}
  ${DATA_GOT+${RELRO_NOW+${GOTPLT}}}
  ${DATA_GOT+${RELRO_NOW-${SEPARATE_GOTPLT+${GOT}}}}
  ${RELOCATING+${DATA_SEGMENT_RELRO_END}}
  ${DATA_GOT+${RELRO_NOW-${SEPARATE_GOTPLT-${GOT}}}}
  ${DATA_GOT+${RELRO_NOW-${GOTPLT}}}

  ${DATA_PLT+${PLT_BEFORE_GOT-${PLT}}}

  .data          :
  {
    ${RELOCATING+${DATA_START_SYMBOLS}}
    *(.data${RELOCATING+ .data.* .gnu.linkonce.d.*})
    ${CONSTRUCTING+SORT(CONSTRUCTORS)}
  }
  .data1         : { *(.data1) }
  ${WRITABLE_RODATA+${RODATA}}
  ${OTHER_READWRITE_SECTIONS}
  ${SMALL_DATA_CTOR+${RELOCATING+${CTOR}}}
  ${SMALL_DATA_DTOR+${RELOCATING+${DTOR}}}
  ${DATA_PLT+${PLT_BEFORE_GOT+${PLT}}}
  ${SDATA_GOT+${RELOCATING+${OTHER_GOT_SYMBOLS}}}
  ${SDATA_GOT+${GOT}}
  ${SDATA_GOT+${OTHER_GOT_SECTIONS}}
  ${SDATA}
  ${OTHER_SDATA_SECTIONS}
  ${RELOCATING+${DATA_END_SYMBOLS-${USER_LABEL_PREFIX}_edata = .; PROVIDE (${USER_LABEL_PREFIX}edata = .);}}
  ${RELOCATING+__bss_start = .;}
  ${RELOCATING+${OTHER_BSS_SYMBOLS}}
  ${SBSS}
  ${BSS_PLT+${PLT}}
  .bss           :
  {
   *(.dynbss)
   *(.bss${RELOCATING+ .bss.* .gnu.linkonce.b.*})
   *(COMMON)
   /* Align here to ensure that the .bss section occupies space up to
      _end.  Align after .bss to ensure correct alignment even if the
      .bss section disappears because there are no input sections.
      FIXME: Why do we need it? When there is no .bss section, we don't
      pad the .data section.  */
   ${RELOCATING+. = ALIGN(. != 0 ? ${ALIGNMENT} : 1);}
  }
  ${OTHER_BSS_SECTIONS}
  ${RELOCATING+${OTHER_BSS_END_SYMBOLS}}
  ${RELOCATING+. = ALIGN(${ALIGNMENT});}
  ${LARGE_SECTIONS}
  ${RELOCATING+. = ALIGN(${ALIGNMENT});}
  ${RELOCATING+${OTHER_END_SYMBOLS}}
  ${RELOCATING+${END_SYMBOLS-${USER_LABEL_PREFIX}_end = .; PROVIDE (${USER_LABEL_PREFIX}end = .);}}
  ${RELOCATING+${DATA_SEGMENT_END}}
EOF

if test -n "${NON_ALLOC_DYN}"; then
  if test -z "${NO_REL_RELOCS}${NO_RELA_RELOCS}"; then
    cat ldscripts/dyntmp.$$
  else
    if test -z "${NO_REL_RELOCS}"; then
      sed -e '/^[ 	]*\.rela\.[^}]*$/,/}/d' -e '/^[ 	]*\.rela\./d' ldscripts/dyntmp.$$
    fi
    if test -z "${NO_RELA_RELOCS}"; then
      sed -e '/^[ 	]*\.rel\.[^}]*$/,/}/d' -e '/^[ 	]*\.rel\./d' ldscripts/dyntmp.$$
    fi
  fi
  rm -f ldscripts/dyntmp.$$
fi

cat <<EOF
  /* Stabs debugging sections.  */
  .stab          0 : { *(.stab) }
  .stabstr       0 : { *(.stabstr) }
  .stab.excl     0 : { *(.stab.excl) }
  .stab.exclstr  0 : { *(.stab.exclstr) }
  .stab.index    0 : { *(.stab.index) }
  .stab.indexstr 0 : { *(.stab.indexstr) }

  .comment       0 : { *(.comment) }

  /* DWARF debug sections.
     Symbols in the DWARF debugging sections are relative to the beginning
     of the section so we begin them at 0.  */

  /* DWARF 1 */
  .debug          0 : { *(.debug) }
  .line           0 : { *(.line) }

  /* GNU DWARF 1 extensions */
  .debug_srcinfo  0 : { *(.debug_srcinfo) }
  .debug_sfnames  0 : { *(.debug_sfnames) }

  /* DWARF 1.1 and DWARF 2 */
  .debug_aranges  0 : { *(.debug_aranges) }
  .debug_pubnames 0 : { *(.debug_pubnames) }

  /* DWARF 2 */
  .debug_info     0 : { *(.debug_info${RELOCATING+ .gnu.linkonce.wi.*}) }
  .debug_abbrev   0 : { *(.debug_abbrev) }
  .debug_line     0 : { *(.debug_line) }
  .debug_frame    0 : { *(.debug_frame) }
  .debug_str      0 : { *(.debug_str) }
  .debug_loc      0 : { *(.debug_loc) }
  .debug_macinfo  0 : { *(.debug_macinfo) }

  /* SGI/MIPS DWARF 2 extensions */
  .debug_weaknames 0 : { *(.debug_weaknames) }
  .debug_funcnames 0 : { *(.debug_funcnames) }
  .debug_typenames 0 : { *(.debug_typenames) }
  .debug_varnames  0 : { *(.debug_varnames) }

  /* DWARF 3 */
  .debug_pubtypes 0 : { *(.debug_pubtypes) }
  .debug_ranges   0 : { *(.debug_ranges) }

  ${TINY_DATA_SECTION}
  ${TINY_BSS_SECTION}

  ${STACK_ADDR+${STACK}}
  ${ATTRS_SECTIONS}
  ${OTHER_SECTIONS}
  ${RELOCATING+${OTHER_SYMBOLS}}
  ${RELOCATING+${DISCARDED}}
}
EOF
