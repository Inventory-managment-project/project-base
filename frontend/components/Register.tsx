import { Logo } from "@/components/icons";
import { Divider } from "@heroui/divider";
import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { EyeClosedIcon, EyeIcon } from "lucide-react";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { user } from "@heroui/theme";

const Register = () => {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [errorUsername, setErrorUsername] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [errorPasswordConfirm, setErrorPasswordConfirm] = useState("");

  const handleRegister = () => {
    setErrorEmail("");
    setErrorPassword("");
    if (username == "") {
      setErrorUsername("Debe ingresar un nombre de usuario");
    }
    if (email == "" || !validateEmail(email)) {
      setErrorEmail("Debe ingresar un email válido");
    }
    if (password != passwordConfirm && password != "" && passwordConfirm != "") {
      setErrorPassword("Las contraseñas no coinciden");
      setErrorPasswordConfirm("Las contraseñas no coinciden");
    }
    if (password == "") {
      setErrorPassword("Debe ingresar una contraseña");
    }
    if (passwordConfirm == "") {
      setErrorPasswordConfirm("Debe confirmar su contraseña");
    }
    if (username != "" && email != "" && password != "" && passwordConfirm != "" && validateEmail(email) && password == passwordConfirm) {
      sendPostRequest(username, email, password);
    }
  };

  const sendPostRequest = async (username: string, email: string, password: string) => {
    try {
      const response = await fetch("http://localhost:8080/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          name: username,
          email: email,
          password: password,
        }),
      });
      const data = await response.json();
      if (response.ok) {
        console.log(data);
        router.push("/panel");
      } else {
        console.error(data);
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
    <Card
      isBlurred
      className="card border-none bg-background/60 dark:bg-default-100/50 w-[400px]"
      shadow="sm"
    >
      <CardHeader className="flex gap-3 flex-row items-center justify-center w-full">
        <Logo />
        <div className="flex flex-col">
          <p className="text-lg font-bold">DFC</p>
          <p className="text-small text-default-500">Registrate para comenzar a adminstrar tiendas</p>
        </div>
      </CardHeader>
      <Divider />
      <CardBody className="flex items-center">
      <Input
          type="text"
          label="Usuario"
          className="max-w-[300px]"
          variant="bordered"
          color="secondary"
          isInvalid={errorUsername !== ""}
          isClearable
          errorMessage={errorUsername}
          onChange={(e) => {
            setUsername(e.target.value);
            setErrorUsername("");
          }}
        />
        <Input
          type="email"
          label="Email"
          className="max-w-[300px] mt-4 mb-4"
          variant="bordered"
          color="secondary"
          isInvalid={errorEmail !== ""}
          isClearable
          errorMessage={errorEmail}
          onChange={(e) => {
            setEmail(e.target.value);
            setErrorEmail("");
          }}
        />
        <Divider className="w-[50%]"/>
        <Input
          type="password"
          label="Contraseña"
          variant="bordered"
          color="secondary"
          className="max-w-[300px] mt-4"
          isInvalid={errorPassword !== ""}
          errorMessage={errorPassword}
          onChange={(e) => {
            setPassword(e.target.value);
            setErrorPassword("");
          }}
        />
        <Input
          type="password"
          label="Confirmar Contraseña"
          variant="bordered"
          color="secondary"
          className="max-w-[300px] mt-4"
          isInvalid={errorPasswordConfirm !== ""}
          errorMessage={errorPasswordConfirm}
          onChange={(e) => {
            setPasswordConfirm(e.target.value);
            setErrorPasswordConfirm("");
          }}
        />
      </CardBody>
      <Divider />
      <CardFooter className="flex flex-row justify-center">
        <Button className="min-w-[200px]" color="secondary" onPress={handleRegister}>
          Registrarse
        </Button>
      </CardFooter>
    </Card>
  );
}

export default Register;