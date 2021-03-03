variable "project_id" {
  default = "serious-energy-304109"
}

variable "region" {
  default = "europe-north1"
}

variable "zone" {
  default = "europe-north1-a"
}

variable "cluster" {
  default = "serious-energy"
}



variable "num_nodes" {
  default     = 4
  description = "number of gke nodes"
}




variable "machine_type" {
  default = "e2-standard-2"
}
variable "disk_size" {
  default = "30"
}

variable "credentials" {
  default = "~/.config/Project-2c482716f3fd.json"
}

variable "kubernetes_min_ver" {
  default = "latest"
}

variable "kubernetes_max_ver" {
  default = "latest"
}