import { useState } from "react";
import { Tabs, Tab } from "@heroui/tabs";
import { motion } from "framer-motion";
import { Image } from "@heroui/image";
import AuthLayout from "@/layouts/auth";
import Login from "@/components/auth/Login";
import Register from "@/components/auth/Register";

export default function Auth() {
  const [selected, setSelected] = useState<number | string>();

  return (
    <AuthLayout>
    <section className="flex flex-col-reverse items-center justify-center h-full w-full gap-4 py-8 md:py-10">
      <div className="w-full flex flex-row justify-center items-center">
       <Tabs
        aria-label="Tabs form"
        className="mb-6 lg:mb-0"
        selectedKey={selected}
        size="md"
        color="secondary"
        onSelectionChange={(key) => {setSelected(key)}}
      >
        <Tab key={0} title="Iniciar Sesión" />
        <Tab key={1} title="Registrarse" />
      </Tabs>
      </div>
      {selected == 0 && (
      <motion.div className="flex flex-row items-center justify-center h-full w-full md:gap-4 py-8 md:py-10"
      initial={{ y: 50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.5 }}
      >
        <Login />
        <Image
        isBlurred
        alt="Logo"
        className="m-0 md:m-5 hidden md:block"
        src="/logo.png"
        width={240}
        />
      </motion.div>
      )}
      {selected == 1 && (
      <motion.div className="flex flex-row-reverse items-center justify-center h-full w-full md:gap-4 py-8 md:py-10"
      initial={{ y: 50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.5 }}
      >
        <Register />
        <Image
        isBlurred
        alt="Logo"
        className="m-0 md:m-5 hidden md:block"
        src="/logo.png"
        width={240}
        />
      </motion.div>
      )}
    </section>
    </AuthLayout>
  );
}
