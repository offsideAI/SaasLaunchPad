# Kobweb Admin Dashboard for SaasLaunchPad

## Steps

```
kobweb create
```


```
╰─❯ kobweb create
✓ Cloning "https://github.com/varabyte/kobweb-templates"... Done!

Press ENTER to select a project to instantiate:

  app: A template for a minimal web app that demonstrates the basic features of Kobweb
> app/empty: A template for building a Kobweb app from an empty skeleton
  library: A template for creating a Kobweb library skeleton
  examples/chat: An example chat app, showcasing a multimodule project layout
  examples/clock: A canvas demo using the Mozilla animated clock canvas tutorial
  examples/imageprocessor: An image processing demo that showcases Kobweb Workers
  examples/jb/counter: A very minimal site with just a counter (based on the Jetbrains tutorial)
  examples/opengl: An OpenGL demo based on the Mozilla textured cube tutorial
  examples/todo: An example TODO app, showcasing client / server interactions

? Specify a folder for your project: 
! The folder you choose here will be created under your current path.
! You can enter `.` if you want to use the current directory.
> saaslaunchpad

? What is the user-visible display title for your project? 
> SaasLaunchPadApi

! Note: The group ID should uniquely identify your project and organization.
! 'io.github.(username).(projectname)' can work for a hobby project.
? What is the group ID for your project? 
> app.saaslaunchpad.saaslaunchpadapp

! 🪡 To learn more: https://github.com/varabyte/kobweb#silk
? Would you like to use Silk, Kobweb's powerful UI layer built on top of Compose for Web? 
> [Yes]  No 

! 🌐 To learn more: https://github.com/varabyte/kobweb#server
? Would you like to include support for defining server behavior via API routes and API streams? 
> [Yes]  No 

! 📝 To learn more: https://github.com/varabyte/kobweb#markdown
? Would you like to include support for handling Markdown files? 
>  Yes  [No]

! ⚙️ To learn more: https://github.com/varabyte/kobweb#worker
? Would you like to include support for a Kobweb worker? 
> [Yes]  No 

✓ Processing templates... Done!
✓ Rearranging site source to conform to the user's package... Done!
✓ Rearranging API source to conform to the user's package... Done!
✓ Removing unused server files (if any)... Done!
✓ Rearranging worker source to conform to the user's package... Done!
✓ Moving worker files to a safe location before cleanup... Done!
✓ Removing unused worker files (if any)... Done!
✓ Finalizing worker files... Done!
✓ Rearranging resources... Done!
✓ Nearly finished. Populating final project... Done!

? Would you like to initialize git for this project? 
>  Yes  [No]

Success! Created empty at /Users/coder/repos/offsideAI/githubrepos/SaasLaunchPad/saaslaunchpad-kobweb-dashboard/empty

╭──────────────────────────────────────────────────────╮
│ Consider downloading IntelliJ IDEA Community Edition │
│ using https://www.jetbrains.com/toolbox-app/         │
╰──────────────────────────────────────────────────────╯

We suggest that you begin by typing:

  cd empty/site
  kobweb run

```