import { Logo } from "@/components/icons";
import { Divider } from "@heroui/divider";
import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { EyeClosedIcon, EyeIcon } from "lucide-react";
import { Tabs, Tab } from "@heroui/tabs";
import { motion } from "framer-motion";
import { Image } from "@heroui/image";
import AuthLayout from "@/layouts/auth";
import Login from "@/components/Login";
import Register from "@/components/Register";

export default function Auth() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [selected, setSelected] = useState<number | string>(0);
  const [errorUsername, setErrorUsername] = useState("");
  const [username, setUsername] = useState("");

  const toggleVisibility = () => setIsVisible(!isVisible);

  const handleLogin = () => {
    setErrorEmail("");
    setErrorPassword("");
    if (email == "" || !validateEmail(email)) {
      setErrorEmail("Debe ingresar un email válido");
    }
    if (password == "") {
      setErrorPassword("Debe ingresar una contraseña");
    }
    if (email != "" && password != "" && validateEmail(email)) {
      sendPostRequest(email, password);
    }
  };

  const sendPostRequest = async (email: string, password: string) => {
    try {
      const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          username: email,
          password: password,
        }),
      });
      const data = await response.json();
      if (response.ok) {
        console.log(data);
        localStorage.setItem("authToken", data.authToken);
        router.push("/panel");
      } else {
        console.error(data);
        setErrorPassword("Email o contraseña incorrectos");
        setErrorEmail("Email o contraseña incorrectos");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  function validateEmail(email : string) {
    var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  return (
    <AuthLayout>
    <section className="flex flex-col-reverse items-center justify-center h-full w-full gap-4 py-8 md:py-10">
      <div className="w-full flex flex-row justify-center items-center">
       <Tabs
        aria-label="Tabs form"
        selectedKey={selected}
        size="md"
        color="secondary"
        onSelectionChange={(key) => {setSelected(key)}}
        defaultSelectedKey={0}
      >
        <Tab key={0} title="Iniciar Sesión" />
        <Tab key={1} title="Registrarse" />
      </Tabs>
      </div>
      {selected == 0 && (
      <motion.div className="flex flex-row items-center justify-center h-full w-full gap-4 py-8 md:py-10"
      initial={{ y: 50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.5 }}
      >
        <Login />
        <Image
        isBlurred
        alt="Logo"
        className="m-5"
        src="/logo.png"
        width={240}
        />
      </motion.div>
      )}
      {selected == 1 && (
      <motion.div className="flex flex-row-reverse items-center justify-center h-full w-full gap-4 py-8 md:py-10"
      initial={{ y: 50, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{ duration: 0.5 }}
      >
        <Register />
        <Image
        isBlurred
        alt="Logo"
        className="m-5"
        src="/logo.png"
        width={240}
        />
      </motion.div>
      )}
    </section>
    </AuthLayout>
  );
}
