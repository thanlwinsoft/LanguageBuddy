;NSIS Modern User Interface
;LanguageBuddy NSIS Installer script
;Written by Keith Stribley

; Some useful definitions that may need changing for different font versions
!ifndef VERSION
  !define VERSION '2.0.0'
!endif

!define APP_NAME 'LanguageBuddy'
!define INSTALL_SUFFIX "ThanLwinSoft.org"
!define WORKSPACE 'LanguageBuddyWorkspace'

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "${APP_NAME} (${VERSION})"
  Caption "A Program for Language Learning."

  OutFile "${APP_NAME}-${VERSION}.exe"
  InstallDir $PROGRAMFILES\${INSTALL_SUFFIX}
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKLM "Software\${INSTALL_SUFFIX}\${APP_NAME}" ""
  
  SetCompressor lzma

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "lgpl-2.1.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH
  
  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
  Icon "../../languagebuddy/plugins/org.thanlwinsoft.languagetest/icons/languageBuddy.ico"
  UninstallIcon "../../languagebuddy/plugins/org.thanlwinsoft.languagetest/icons/uninstallLanguageBuddy.ico"
;Installer Sections

Function findJavaHome
	StrCpy $0 0
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment\1.8" "JavaHome"
	  StrCmp $1 "" 0 done
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment\1.7" "JavaHome"
	  StrCmp $1 "" 0 done
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Runtime Environment\1.6" "JavaHome"
	  StrCmp $1 "" 0 done
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Development Kit\1.8" "JavaHome"
	  StrCmp $1 "" 0 done
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Development Kit\1.7" "JavaHome"
	  StrCmp $1 "" 0 done
	ReadRegStr $1 HKLM "Software\JavaSoft\Java Development Kit\1.6" "JavaHome"
	  StrCmp $1 "" noJava done
	
	noJava:
	  StrCpy $1 ""
	  MessageBox MB_YESNO|MB_ICONSTOP "No Java 1.5 Runtime was found. ${APP_NAME} will not work unless Java 1.5 or later is installed. Please download a Java Runtime Environment from http://java.com/en/download/ and rerun this installer. Do you want to proceed anyway?" IDYES done IDNO quit
	quit: 
	Quit
	done:
	Push $1
FunctionEnd

Section "-!${APP_NAME}" SecApp

  Call findJavaHome
  Var /GLOBAL JAVA_HOME
  Pop $JAVA_HOME
  
  
  
  IfFileExists "$INSTDIR" 0 BranchNoExist
    
    MessageBox MB_YESNO|MB_ICONQUESTION "Would you like to overwrite existing ${APP_NAME} directory?" IDNO  NoOverwrite ; skipped if file doesn't exist

    
    BranchNoExist:
  
    SetOverwrite on ; NOT AN INSTRUCTION, NOT COUNTED IN SKIPPINGS

NoOverwrite:

  
  SetOutPath "$INSTDIR"
  File /oname=license.txt "lgpl-2.1.txt"

  !cd "../../languagebuddy"
  ;File /r "..\release\win32.win32.x86\${APP_NAME}"
  File /r "${APP_NAME}"
  
  SetOutPath "$INSTDIR\${APP_NAME}"
  File "plugins\org.thanlwinsoft.languagetest\icons\languageBuddy.ico"
  ;File "Uninstall.ico"
  
  CreateDirectory "$INSTDIR\${APP_NAME}\features"
  SetOutPath "$INSTDIR\${APP_NAME}\features"
  
  ClearErrors
  ; write a batch file that sets the Java home
  FileOpen $0 "$INSTDIR\${APP_NAME}\setJavaHome.bat" w
  FileWrite $0 "set JAVA_HOME=$JAVA_HOME"
  FileWriteByte $0 "13"
  FileWriteByte $0 "10"
  FileClose $0
  IfErrors doneJavaHomeError doneJavaHomeBat
  doneJavaHomeError:
  	MessageBox MB_OK|MB_ICONEXCLAMATION "Failed to write setJavaHome.bat"
  doneJavaHomeBat:
  
  SetShellVarContext all
  ; set up shortcuts
  CreateDirectory "$SMPROGRAMS\${APP_NAME}"
  CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME}.lnk" \
	"$INSTDIR\${APP_NAME}\eclipse\launcher.exe" '-vm "$JAVA_HOME\bin\javaw.exe" -product org.thanlwinsoft.languagetest.LanguageTest -data "${APPDATA}/${WORKSPACE}"' \
	"$INSTDIR\${APP_NAME}\languageBuddy.ico" 0 SW_SHOWNORMAL \
	"" "${APP_NAME}"
;  CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME}Uninstall.lnk" \
;	"$INSTDIR\${APP_NAME}\Uninstall.exe" "" \
;	"$INSTDIR\${APP_NAME}\UninstallDCC16.ico" 0 SW_SHOWNORMAL \
;	"" "Uninstall ${APP_NAME}"

  CreateShortCut "$DESKTOP\${APP_NAME}.lnk" \
	"$INSTDIR\${APP_NAME}\eclipse\launcher.exe" '-vm "$JAVA_HOME\bin\javaw.exe" -product org.thanlwinsoft.languagetest.LanguageTest -data "${APPDATA}/${WORKSPACE}"' \
	"$INSTDIR\${APP_NAME}\languageBuddy.ico" 0 SW_SHOWNORMAL \
	"" "${APP_NAME}"
	
  ;Store installation folder
  WriteRegStr HKLM "Software\${INSTALL_SUFFIX}\${APP_NAME}" "" $INSTDIR

  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\${APP_NAME}\Uninstall.exe"

  ; add keys for Add/Remove Programs entry
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" \
                 "DisplayName" "${APP_NAME} ${VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" \
                 "UninstallString" "$INSTDIR\${APP_NAME}\Uninstall.exe"
				 
  

SectionEnd

;Optional source - as a compressed archive
Section /o "Source" SecSource
	SetOutPath "$INSTDIR\${APP_NAME}"
	File languagebuddy-*.tar.bz2
SectionEnd


;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecApp ${LANG_ENGLISH} "Install the ${APP_NAME} (version ${VERSION})."

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecApp} $(DESC_SecApp)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

Function .onInstFailed
	MessageBox MB_OK "You may want to rerun the installer as an Administrator or specify a different installation directory."

FunctionEnd

Function .onInstSuccess

	Exec '"$INSTDIR\${APP_NAME}\eclipse\launcher.exe" -vm "$JAVA_HOME\bin\javaw.exe"  -product org.thanlwinsoft.languagetest.LanguageTest -data "${APPDATA}/${WORKSPACE}"'

FunctionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"
  SetShellVarContext all
  
  IfFileExists "$INSTDIR" AppFound 0
    MessageBox MB_OK|MB_ICONEXCLAMATION "$INSTDIR\${APP_NAME} was not found! You may need to uninstall manually." 
	
AppFound:
  RMDir /r "$INSTDIR\configuration"
  RMDir /REBOOTOK /r "$INSTDIR\plugins"
  RMDir /REBOOTOK /r "$INSTDIR\features"
  Delete /REBOOTOK "$INSTDIR\launcher.ini"
  Delete /REBOOTOK "$INSTDIR\launcher.exe"
  Delete /REBOOTOK "$INSTDIR\setJavaHome.bat"
  Delete /REBOOTOK "$INSTDIR\lgpl-2.1.txt"
  
  ;Delete "$INSTDIR\${SRC_ARCHIVE}"
  Delete /REBOOTOK "$INSTDIR\Uninstall.exe"

  RMDir "$INSTDIR"
  
  Delete  "$DESKTOP\${APP_NAME}.lnk"
  RMDir /r "$SMPROGRAMS\${APP_NAME}"

  DeleteRegKey /ifempty HKLM "Software\${INSTALL_SUFFIX}"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}"
  
  IfFileExists "$INSTDIR\workspace" 0 end
    MessageBox MB_YESNO|MB_ICONEXCLAMATION "$INSTDIR\workspace exists. This may contain some of your own files. Do you want to remove it as well?" IDYES 0 IDNO end
  
  RMDir /REBOOTOK /r "$INSTDIR"

end:

SectionEnd

