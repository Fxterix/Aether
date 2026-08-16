# Enable modules to include each other's files
include_directories(.)

# CMake seems to only define _DEBUG on Windows
set_property(DIRECTORY APPEND PROPERTY
    COMPILE_DEFINITIONS $<$<CONFIG:Debug>:_DEBUG> $<$<NOT:$<CONFIG:Debug>>:NDEBUG>)

if (ENABLE_LTO)
    set(CMAKE_INTERPROCEDURAL_OPTIMIZATION TRUE)
endif()

# Set compilation flags
if (MSVC)
    set(CMAKE_CONFIGURATION_TYPES Debug Release CACHE STRING "" FORCE)

    # Silence "deprecation" warnings
    add_definitions(-D_CRT_SECURE_NO_WARNINGS -D_CRT_NONSTDC_NO_DEPRECATE -D_SCL_SECURE_NO_WARNINGS)

    # Avoid windows.h junk
    add_definitions(-DNOMINMAX)

    # Avoid windows.h from including some usually unused libs like winsocks.h, since this might cause some redefinition errors.
    add_definitions(-DWIN32_LEAN_AND_MEAN)

    # Ensure that projects build with Unicode support.
    add_definitions(-DUNICODE -D_UNICODE)

    add_compile_options(
        /MP
        /Zm200
        /Zo
        /permissive-
        /EHsc
        /utf-8
        /volatile:iso
        /Zc:externConstexpr
        /Zc:inline
        /Zc:throwingNew
        /GT

        # Some flags for more deterministic builds, to aid caching.
        /experimental:deterministic
        /d1trimfile:"${CMAKE_SOURCE_DIR}"

        # External headers diagnostics
        /experimental:external
        /external:anglebrackets
        /external:W0

        # Warnings
        /W3

        /we4062
        /we4189
        /we4265
        /we4388
        /we4389
        /we4456
        /we4457
        /we4459
        /we4505
        /we4547
        /we4549
        /we4555
        /we4826
        /we5038
        /we5233
        /we5245

        /wd4100
        /wd4324
    )

    if (AETHER_WARNINGS_AS_ERRORS)
        add_compile_options(/WX)
    endif()

    if (CMAKE_C_COMPILER_LAUNCHER STREQUAL "ccache" OR CMAKE_CXX_COMPILER_LAUNCHER STREQUAL "ccache"
            OR AETHER_USE_PRECOMPILED_HEADERS)
        add_compile_options(/Z7)
    else()
        add_compile_options(/Zi)
    endif()

    add_compile_options("$<$<CONFIG:Release>:/GS->")

    set(CMAKE_EXE_LINKER_FLAGS_DEBUG   "/DEBUG /MANIFEST:NO" CACHE STRING "" FORCE)
    set(CMAKE_EXE_LINKER_FLAGS_RELEASE "/DEBUG /MANIFEST:NO /INCREMENTAL:NO /OPT:REF,ICF /PDBALTPATH:%_PDB%" CACHE STRING "" FORCE)
else()
    add_compile_options(
        -Wall
        -Wno-unused-command-line-argument
        -Wp,-D_GLIBCXX_ASSERTIONS
        -fstack-protector-strong
        -fstack-clash-protection
    )

    if (NOT CMAKE_BUILD_TYPE STREQUAL Debug)
        add_compile_options(-Wp,-D_FORTIFY_SOURCE=2)
    endif()

    if (AETHER_WARNINGS_AS_ERRORS)
        add_compile_options(-Werror)
    endif()

    if (APPLE AND CMAKE_CXX_COMPILER_ID STREQUAL Clang)
        add_compile_options("-stdlib=libc++")
    endif()

    if (CMAKE_CXX_COMPILER_ID STREQUAL GNU)
        add_compile_options("-Wno-attributes")
        add_compile_options("-Wno-interference-size")
    endif()

    if (MINGW)
        add_definitions(-DMINGW_HAS_SECURE_API)
        if (COMPILE_WITH_DWARF)
            add_compile_options("-gdwarf")
        endif()
    endif()

    if(CMAKE_SYSTEM_NAME STREQUAL "Linux" OR MINGW)
        add_definitions(-D_FILE_OFFSET_BITS=64)

        set(CMAKE_C_ARCHIVE_CREATE   "<CMAKE_AR> qcTP <TARGET> <LINK_FLAGS> <OBJECTS>")
        set(CMAKE_C_ARCHIVE_APPEND   "<CMAKE_AR> qTP  <TARGET> <LINK_FLAGS> <OBJECTS>")
        set(CMAKE_CXX_ARCHIVE_CREATE "<CMAKE_AR> qcTP <TARGET> <LINK_FLAGS> <OBJECTS>")
        set(CMAKE_CXX_ARCHIVE_APPEND "<CMAKE_AR> qTP  <TARGET> <LINK_FLAGS> <OBJECTS>")
    endif()
endif()

if(ENABLE_SOFTWARE_RENDERER)
    add_compile_definitions(ENABLE_SOFTWARE_RENDERER)
endif()
if(ENABLE_OPENGL)
    add_compile_definitions(ENABLE_OPENGL)
endif()
if(ENABLE_VULKAN)
    add_compile_definitions(ENABLE_VULKAN)
endif()

add_subdirectory(common)
add_subdirectory(core)
add_subdirectory(video_core)
add_subdirectory(audio_core)
add_subdirectory(network)
add_subdirectory(input_common)

if (ENABLE_TESTS)
    add_subdirectory(tests)
endif()

if (ENABLE_SDL2 AND ENABLE_SDL2_FRONTEND)
    add_subdirectory(aether)
endif()

if (ENABLE_QT)
    add_subdirectory(aether_qt)
endif()

if (ENABLE_DEDICATED_ROOM)
    add_subdirectory(dedicated_room)
endif()

if (ANDROID)
    add_subdirectory(android/app/src/main/jni)
    target_include_directories(aether-android PRIVATE android/app/src/main)
endif()

if (ENABLE_WEB_SERVICE)
    add_subdirectory(web_service)
endif()
