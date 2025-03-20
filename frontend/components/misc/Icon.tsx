import { LucideProps } from "lucide-react";
import dynamicIconImports from "lucide-react/dynamicIconImports";
import { useState, useEffect } from "react";
import { ForwardRefExoticComponent, RefAttributes } from "react";

interface IconProps extends LucideProps {
  name: keyof typeof dynamicIconImports;
}

export const Icon = ({ name, ...props }: IconProps) => {
  const [LucideIcon, setLucideIcon] = useState<ForwardRefExoticComponent<
    Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>
  > | null>(null);

  useEffect(() => {
    const loadIcon = async () => {
      try {
        const IconComponent = (await dynamicIconImports[name]()).default;
        setLucideIcon(IconComponent);
      } catch (error) {
        console.error("Error loading icon", error);
      }
    };

    loadIcon();
  }, [name]);

  if (!LucideIcon) return null;

  return <LucideIcon {...props} />;
};
