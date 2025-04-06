import { Link } from "@heroui/link";
import { Snippet } from "@heroui/snippet";
import { Code } from "@heroui/code";
import { button as buttonStyles } from "@heroui/theme";

import { siteConfig } from "@/config/site";
import { title, subtitle } from "@/components/misc/primitives";
import { GithubIcon, ArrowRightIcon } from "@/components/misc/icons";
import LandingLayout from "@/layouts/landing";

export default function IndexPage() {
  return (
    <LandingLayout>
      <section className="flex flex-col items-center justify-center gap-6 py-16 md:py-24 bg-gradient-to-br from-violet-500 via-purple-400 to-indigo-600 text-white rounded-xl shadow-xl mx-4 my-8">
        <div className="flex flex-col items-center max-w-xl text-center">
          <span className={`${subtitle()} uppercase text-white tracking-widest font-light mb-3`}>
            Bienvenido a nuestro
          </span>
          <h1 className={`${title()} text-white text-4xl md:text-5xl lg:text-6xl font-bold tracking-tight relative mb-1`}>
            <span className="text-white">
              Punto de Venta
            </span>
            <span className="absolute -bottom-3 left-0 right-0 h-1 bg-gradient-to-r from-white/50 via-white to-white/50 rounded-full"></span>
          </h1>
          <span className="text-white text-sm font-medium tracking-wide mt-5">GESTIÓN EMPRESARIAL SIMPLIFICADA</span>
        </div>
        
        <p className="text-center text-white/95 max-w-2lg px-6 mb-8 text-lg mt-4 leading-relaxed">
          Gestiona tu negocio de forma eficiente con nuestra solución completa para ventas, inventario, análisis y clientes.
        </p>
        
        <div className="flex flex-col sm:flex-row gap-4 mt-4">
          <Link
            href="https://github.com/Inventory-managment-project/project-base"
            className="flex items-center gap-2 px-6 py-3 font-medium bg-white text-violet-700 rounded-full hover:bg-gray-100 transition-colors shadow-lg"
            target="_blank"
            rel="noopener noreferrer"
          >
            <GithubIcon className="w-5 h-5" />
            Ver en GitHub
          </Link>
          
          <Link
            href="/auth"
            className="flex items-center justify-center gap-2 px-6 py-3 font-medium bg-white text-violet-700 rounded-full hover:bg-gray-50 transition-all transform hover:scale-105 shadow-lg border border-white/20 group"
          >
            Comienza aquí
            <ArrowRightIcon className="w-4 h-4 transition-transform group-hover:translate-x-1" />
          </Link>
        </div>
      </section>
    </LandingLayout>
  );
}
