import { motion, AnimatePresence } from 'framer-motion'
import { useFrame } from '@/context/FrameContext'
import { GithubIcon } from "@/components/misc/icons"
import { ArrowRightIcon } from "lucide-react"
import Link from 'next/link'
import { ChevronsDown } from "lucide-react"
import { scrollTo } from '@/components/utils/utils'

export function HeroSection() {
  const { frame } = useFrame()

  const visible = frame >= 0 && frame <= 40

  return (
    <AnimatePresence>
        <motion.section
          id="home"
          initial={{ opacity: 0 }}
          animate={{ opacity: visible ? 1 : 0 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.9, ease: "easeInOut" }}
          className="z-10 flex flex-col items-center justify-center gap-6 py-16 md:py-24 mx-4 my-8"
        >
          <div className="flex flex-col items-center max-w-xl text-center">
            <span className="text-2xl uppercase tracking-widest font-bold mb-3">
              Bienvenido a
            </span>
            <h1 className="text-3xl md:text-5xl lg:text-6xl font-bold tracking-tight relative mb-1 text-violet-700">
              DFC
            </h1>
            <span className="text-md font-bold tracking-wide mt-5">
              GESTIÓN EMPRESARIAL SIMPLIFICADA
            </span>
          </div>

          <p className="text-center max-w-2lg px-6 mb-8 text-lg mt-4 leading-relaxed">
            Gestiona tu negocio de forma eficiente con nuestra solución completa para ventas, inventario, análisis y clientes.
          </p>

          <div className="flex flex-col sm:flex-row gap-4 mt-4">
            <Link
              href="https://github.com/Inventory-managment-project/project-base"
              className="flex items-center gap-2 px-6 py-3 font-medium bg-violet-500 text-black rounded-full hover:bg-violet-600 transition-colors shadow-lg"
              target="_blank"
              rel="noopener noreferrer"
            >
              <GithubIcon className="w-5 h-5" />
              Ver en GitHub
            </Link>

            <Link
              href="/auth"
              className="flex items-center gap-2 px-6 py-3 font-medium bg-violet-500 text-black rounded-full hover:bg-violet-600 transition-colors shadow-lg"
            >
              Comienza aquí
              <ArrowRightIcon className="w-4 h-4 transition-transform group-hover:translate-x-1" />
            </Link>
          </div>
          <motion.div
            className="mt-12 flex justify-center"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: visible ? 1 : 0, y: visible ? [0, 10, 0] : 10 }}
            transition={{
              opacity: { duration: 0.5 },
              y: {
                duration: 1.5,
                repeat: Infinity,
                ease: "easeInOut"
              }
            }}
          >
            <button onClick={() => scrollTo("features")}>
              <ChevronsDown size={40} className="text-violet-600 animate-bounce" />
            </button>
          </motion.div>
        </motion.section>
      
    </AnimatePresence>
  )
}
