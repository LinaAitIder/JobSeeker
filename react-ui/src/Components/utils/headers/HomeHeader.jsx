import React from 'react'
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import { AcademicCapIcon, BuildingOfficeIcon } from '@heroicons/react/24/outline'
import { ChevronDownIcon } from '@heroicons/react/24/solid'

const HomeHeader = () => {
  return (
    <header className="p-4 bg-blue-500 ">
      <div className="flex flex-row items-center justify-between">
        <span className="text-white font-serif text-2xl pl-4 hover:cursor-pointer" onClick={()=>{
          window.location.href='/'}}>Job Seeker</span>
       <div className='flex flex-row justify-between items-center'>
       <button className="text-black  bg-white border-0 px-6 py-0 text-sm h-11 font-serif rounded-full " onClick={()=>{window.location.href='/login'}}>Login</button>
       <div className="ml-2">
        <Menu> 
        <MenuButton className="text-black font-serif text-sm bg-white px-5 py-3 rounded-md inline-flex gap-2  ">Space
        <ChevronDownIcon className="size-4 fill-black/60" />

        </MenuButton>
        <MenuItems anchor="bottom end" className=" bg-gray-100 rounded-md shadow-lg p-1 ring-opacity-5 focus:outline-none">
          <MenuItem className="pr-7 mt-2">
            <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={()=>{window.location.href='/candidateSignUp'}}>
              <AcademicCapIcon className="text-black size-4 "/> 
              Candidate Space
            </button>
          </MenuItem>
          <MenuItem>
            <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={()=>{window.location.href='/recruiterSignUp'}}>
              <BuildingOfficeIcon className="text-black size-4"/>Organisation Space
            </button>
          </MenuItem>
        </MenuItems>
        </Menu>
       </div>
      </div>
      </div>
    </header>
  )
}

export default HomeHeader;