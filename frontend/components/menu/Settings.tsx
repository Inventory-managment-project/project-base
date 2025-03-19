import { title, subtitle } from "@/components/misc/primitives";
import { ThemeSwitch } from "@/components/misc/theme-switch";

const Setting = () => {
  return (
    <div className="flex flex-col w-[50%] select-none max-md:w-[85%] h-full gap-4 justify-start items-center">
      <h1 className={title()}>Configuración</h1>
      <div className="flex flex-row justify-between w-full">
        <p className={subtitle()}>Visualización:</p>
        <ThemeSwitch />
      </div>
    </div>
  );
}

export default Setting; 
