import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
  NavbarItem,
} from "@heroui/navbar";

import { ThemeSwitch } from "@/components/misc/theme-switch";
import { Logo } from "@/components/misc/icons";
import { Button } from "@heroui/button";

export const Navbar = () => {
  return (
    <NextUINavbar maxWidth="xl" position="sticky">
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <NavbarBrand as="li" className="gap-3 max-w-fit">
          <div className="flex justify-start items-center gap-1">
            <Logo />
            <p className="font-bold text-xl text-inherit tracking-[.25em]">DFC</p>
          </div>
        </NavbarBrand>
      </NavbarContent>
    </NextUINavbar>
  );
};
